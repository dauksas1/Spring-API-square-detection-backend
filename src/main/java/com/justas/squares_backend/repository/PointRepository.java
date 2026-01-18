package com.justas.squares_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.justas.squares_backend.entities.Point;

public interface PointRepository extends JpaRepository<Point, Long>{
	
	Optional<Point> findByXAndY(int x, int y);

	boolean existsByXAndY(int x, int y);

}
