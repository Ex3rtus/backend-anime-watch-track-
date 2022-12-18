package self.project.animewatchtrack.validation.constraints;

import self.project.animewatchtrack.validation.validators.NullOrPositiveValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Youssef Kaïdi.
 * created 17 déc. 2022.
 */

@Target(PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = NullOrPositiveValidator.class)
@Documented
public @interface NullOrPositive {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
