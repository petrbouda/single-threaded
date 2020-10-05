package pbouda.singlethreaded;

import javax.validation.*;
import java.util.Set;

public class Validators {

    private static final Validator VALIDATOR;

    static {
        Configuration<?> configuration = Validation
                .byDefaultProvider()
                .configure();

        ValidatorFactory validatorFactory = configuration.buildValidatorFactory();
        VALIDATOR = validatorFactory.getValidator();
    }

    public static <T> boolean validate(T request) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(request);
        return violations.isEmpty();
    }
}
