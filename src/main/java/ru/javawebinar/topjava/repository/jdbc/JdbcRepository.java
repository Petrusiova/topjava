package ru.javawebinar.topjava.repository.jdbc;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.User;

import javax.validation.*;
import java.util.Set;

public abstract class JdbcRepository {

    protected void validate(AbstractBaseEntity abstractBaseEntity){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AbstractBaseEntity>> violations = validator.validate(abstractBaseEntity);
        if (violations.size() > 0){
            for (ConstraintViolation<AbstractBaseEntity> violation : violations) {
                throw new ValidationException(violation.getMessage());
            }
        }
    }
}
