package com.justas.squares_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justas.squares_backend.dto.SquareDTO;
import com.justas.squares_backend.services.SquareService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Squares", description = "Endpoints for square detection")
@RestController
@RequestMapping("/squares")
public class SquareController {
	
	private final SquareService squareService;
	
	public SquareController(SquareService squareService) {
		this.squareService = squareService;
	}
	
	
	@Operation(
	        summary = "Get all detected squares",
	        description = "Returns a list of all squares found in the dataset"
	    )
	@GetMapping
	public ResponseEntity<List<SquareDTO>> getSquares(){
		List<SquareDTO> squares = squareService.findSquares();
		return ResponseEntity.ok(squares);
	}
	
	
	@Operation(
	        summary = "Get square count",
	        description = "Returns the total number of detected squares"
	    )
	@GetMapping("/count")
	public int getSquareCount() {
	    return squareService.countSquares();
	}


}
