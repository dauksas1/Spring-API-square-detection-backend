package com.justas.squares_backend.mappers;

import com.justas.squares_backend.dto.PointInputDTO;
import com.justas.squares_backend.dto.PointOutputDTO;
import com.justas.squares_backend.entities.Point;

public class PointMapper {
	
	public static Point toEntity(PointInputDTO dto) {
		return new Point(dto.x(), dto.y());
	}
	
	public static PointOutputDTO toOutputDTO(Point entity) {
		return new PointOutputDTO(entity.getId(), entity.getX(), entity.getY());
	}

}
