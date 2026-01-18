package com.justas.squares_backend.exceptions;

import com.justas.squares_backend.dto.PointInputDTO;

@SuppressWarnings("serial")
public class PointNotFoundException extends RuntimeException {

    public PointNotFoundException(PointInputDTO input) {
        super("Point not found: (" + input.x() + ", " + input.y() + ")");
    }
}

