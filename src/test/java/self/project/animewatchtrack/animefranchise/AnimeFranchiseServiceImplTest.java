package self.project.animewatchtrack.animefranchise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author Youssef Kaïdi.
 * created 28 oct. 2022.
 */

@ExtendWith(MockitoExtension.class)
class AnimeFranchiseServiceImplTest {
    @Mock
    private AnimeFranchiseRepository franchiseRepository;
    private AnimeFranchiseServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new AnimeFranchiseServiceImpl(franchiseRepository);
    }

    @Test
    void itShouldReturnAnEmptyAnimeFranchiseList() {
        List<AnimeFranchiseDTO> result = underTest.getAll();
        verify(franchiseRepository).findAll();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void itShouldReturnAPopulatedAnimeFranchiseList() {
        AnimeFranchise franchise1 = AnimeFranchise.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 1")
                .hasBeenWatched(false)
                .build();

        AnimeFranchise franchise2 = AnimeFranchise.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 2")
                .hasBeenWatched(true)
                .build();

        List<AnimeFranchise> fixture = Arrays.asList(franchise1, franchise2);
        when(franchiseRepository.findAll()).thenReturn(fixture);

        List<AnimeFranchiseDTO> expected = Stream.of(franchise1, franchise2)
                .map(AnimeFranchiseMapper::mapToDTO)
                .collect(Collectors.toList());

        List<AnimeFranchiseDTO> result = underTest.getAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldGetFranchiseById() {
        String id = UUID.randomUUID().toString();
        AnimeFranchise franchiseToFind = AnimeFranchise.builder()
                .id(id)
                .franchiseTitle("Franchise To Find")
                .hasBeenWatched(false)
                .build();

        AnimeFranchiseDTO expected = AnimeFranchiseMapper.mapToDTO(franchiseToFind);

        when(franchiseRepository.findById(franchiseToFind.getId())).thenReturn(Optional.of(franchiseToFind));

        assertThat(underTest.getById(id)).isEqualTo(expected);
        verify(franchiseRepository).findById(id);
    }

    @Test
    void itShouldThrowWhenTryingToFindByInvalidId() {
        String fakeUUID = "Believe me, i'm a uuid turned into a string";
        String exceptionMessage = "anime franchise with ID : " + fakeUUID + " not found";
        RuntimeException exceptionToBeThrown = new RuntimeException(exceptionMessage);

        when(franchiseRepository.findById(fakeUUID))
                .thenThrow(exceptionToBeThrown);

        assertThatThrownBy(() -> underTest.getById(fakeUUID))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldAddAnimeFranchise() {
        //given
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand("Can Be Added", false);
        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);

        //when
        underTest.addAnimeFranchise(animeFranchiseCommand);

        //then
        ArgumentCaptor<AnimeFranchise> animeFranchiseArgumentCaptor =
                ArgumentCaptor.forClass(AnimeFranchise.class);

        verify(franchiseRepository)
                .save(animeFranchiseArgumentCaptor.capture());

        AnimeFranchise capturedFranchise = animeFranchiseArgumentCaptor.getValue();
        assertThat(capturedFranchise).isEqualTo(animeFranchise);
    }

    @Test
    void itShouldThrowWhenAttemptingToCreateAnAnimeFranchiseThatExists() {
        //given
        String existingFranchiseTitle = "Already Existing Franchise";
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand(existingFranchiseTitle, false);

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);

        when(franchiseRepository.findByFranchiseTitle(existingFranchiseTitle))
                .thenReturn(Optional.of(animeFranchise));

        assertThatThrownBy(() -> underTest.addAnimeFranchise(animeFranchiseCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("anime franchise with title " + existingFranchiseTitle + " already exists");

        verify(franchiseRepository, never()).save(any());
    }
}