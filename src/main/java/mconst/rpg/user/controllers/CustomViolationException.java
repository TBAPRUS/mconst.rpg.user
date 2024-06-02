package mconst.rpg.user.controllers;

import jakarta.validation.ValidationException;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomViolationException extends ValidationException {
    List<Violation> violations;

    public CustomViolationException(String message, List<Violation> violations) {
        super(message);
        this.violations = violations;
    }
}
