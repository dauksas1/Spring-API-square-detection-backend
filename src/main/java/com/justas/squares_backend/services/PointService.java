package com.justas.squares_backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.justas.squares_backend.dto.PointInputDTO;
import com.justas.squares_backend.dto.PointOutputDTO;
import com.justas.squares_backend.entities.Point;
import com.justas.squares_backend.exceptions.PointNotFoundException;
import com.justas.squares_backend.mappers.PointMapper;
import com.justas.squares_backend.repository.PointRepository;

@Service
public class PointService {
	
	private final PointRepository repository;
	
	public PointService(PointRepository repository) {
		this.repository = repository;
	}
	
	public PointOutputDTO addPoint(PointInputDTO input) {

        if (repository.existsByXAndY(input.x(), input.y())) {
            throw new IllegalStateException(
                    "Duplicates not allowed. Duplicate point (" + input.x() + ", " + input.y() + ") already exists"
            );
        }

        Point saved = repository.save(PointMapper.toEntity(input));
        return PointMapper.toOutputDTO(saved);
    }

	public PointOutputDTO deletePointByCoordinates(PointInputDTO input) {

	    Point point = repository.findByXAndY(input.x(), input.y())
	            .orElseThrow(() -> new PointNotFoundException(input));

	    repository.delete(point);

	    return PointMapper.toOutputDTO(point);
	}



    public List<PointOutputDTO> addPoints(List<PointInputDTO> pointInputDTOList) {

        List<PointInputDTO> duplicates = new ArrayList<>();

        for (PointInputDTO pointInputDTO : pointInputDTOList) {
            if (repository.existsByXAndY(pointInputDTO.x(), pointInputDTO.y())) {
                duplicates.add(pointInputDTO);
            }
        }

        if (!duplicates.isEmpty()) {
            throw new IllegalStateException(
                    "Duplcates not allowed. Duplicate points found: " + duplicates.toString()
            );
        }

        List<Point> saved = pointInputDTOList.stream()
                .map(PointMapper::toEntity)
                .map(repository::save)
                .toList();

        return saved.stream()
                .map(PointMapper::toOutputDTO)
                .toList();
    }



    public List<PointOutputDTO> getAllPoints() {
        return repository.findAll().stream()
                .map(PointMapper::toOutputDTO)
                .toList();
    }

}
