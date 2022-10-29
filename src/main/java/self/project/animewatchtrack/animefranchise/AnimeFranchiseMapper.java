package self.project.animewatchtrack.animefranchise;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public class AnimeFranchiseMapper {
    public static AnimeFranchise mapToEntity(AnimeFranchiseCommand animeFranchiseCommand) {
        return AnimeFranchise.builder()
                .franchiseTitle(animeFranchiseCommand.getFranchiseTitle())
                .hasBeenWatched(animeFranchiseCommand.isHasBeenWatched())
                .build();
    }

    public static AnimeFranchiseDTO mapToDTO(AnimeFranchise animeFranchise) {
        return AnimeFranchiseDTO.builder()
                .id(animeFranchise.getId())
                .franchiseTitle(animeFranchise.getFranchiseTitle())
                .hasBeenWatched(animeFranchise.isHasBeenWatched())
                .build();
    }
}