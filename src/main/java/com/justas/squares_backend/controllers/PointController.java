package com.justas.squares_backend.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justas.squares_backend.dto.PointInputDTO;
import com.justas.squares_backend.dto.PointOutputDTO;
import com.justas.squares_backend.services.PointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(
    name = "Points",
    description = "Endpoints for managing points in the system"
)

@RestController
@RequestMapping("/points")
public class PointController {

    private final PointService service;

    public PointController(PointService service) {
        this.service = service;
    }

    
    @Operation(
        summary = "Add a single point",
        description = "Creates a new point using the provided coordinates"
    )
    @ApiResponse(responseCode = "201", description = "Point created successfully")
    @PostMapping("/single")
    public ResponseEntity<PointOutputDTO> addPoint(@Valid @RequestBody PointInputDTO input) {
        PointOutputDTO saved = service.addPoint(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    
    @Operation(
        summary = "Delete a single point",
        description = "Deletes a point by its coordinates if it exists"
    )
    @ApiResponse(responseCode = "204", description = "Point deleted successfully")
    @ApiResponse(responseCode = "404", description = "Point not found")
    @DeleteMapping("/single")
    public ResponseEntity<Void> deletePoint(@Valid @RequestBody PointInputDTO input) {
        service.deletePointByCoordinates(input);
        return ResponseEntity.noContent().build();
    }

    
    @Operation(
        summary = "Import multiple points",
        description = "Adds a list of points in a single request"
    )
    @ApiResponse(responseCode = "201", description = "Points imported successfully")
    @PostMapping
    public ResponseEntity<List<PointOutputDTO>> importPoints(
            @RequestBody List<@Valid PointInputDTO> points
    ) {
        List<PointOutputDTO> saved = service.addPoints(points);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    
    @Operation(
        summary = "Get all points",
        description = "Returns a list of all stored points"
    )
    @GetMapping
    public List<PointOutputDTO> getAllPoints() {
        return service.getAllPoints();
    }
}

