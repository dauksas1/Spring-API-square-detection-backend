package com.justas.squares_backend.dto;

import jakarta.validation.constraints.NotNull;

public record PointInputDTO(
	    @NotNull(message = "X coordinate must not be null")
	    Integer x,

	    @NotNull(message = "Y coordinate must not be null")
	    Integer y
	) {}
