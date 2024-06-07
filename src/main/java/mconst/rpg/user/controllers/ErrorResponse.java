package mconst.rpg.user.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final List<Violation> violations;
}
