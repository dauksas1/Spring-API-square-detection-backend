package com.justas.squares_backend.services;

import com.justas.squares_backend.dto.PointInputDTO;
import com.justas.squares_backend.dto.PointOutputDTO;
import com.justas.squares_backend.entities.Point;
import com.justas.squares_backend.exceptions.PointNotFoundException;
import com.justas.squares_backend.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    private PointRepository repository;
    private PointService pointService;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(PointRepository.class);
        pointService = new PointService(repository);
    }

    private PointInputDTO dto(int x, int y) {
        return new PointInputDTO(x, y);
    }

    private Point entity(int x, int y) {
        return new Point(x, y);
    }

    // ------------------------------------------------------------
    // 1. ADD POINT
    @Test
    void addPoint_savesPointWhenNotDuplicate() {
        PointInputDTO input = dto(1, 2);
        Point saved = entity(1, 2);

        when(repository.existsByXAndY(1, 2)).thenReturn(false);
        when(repository.save(any(Point.class))).thenReturn(saved);

        PointOutputDTO result = pointService.addPoint(input);

        assertThat(result.x()).isEqualTo(1);
        assertThat(result.y()).isEqualTo(2);

        verify(repository).save(any(Point.class));
    }

    // ------------------------------------------------------------
    // 2. ADD POINT - DUPLICATE
    @Test
    void addPoint_throwsExceptionWhenDuplicate() {
        PointInputDTO input = dto(1, 2);

        when(repository.existsByXAndY(1, 2)).thenReturn(true);

        assertThatThrownBy(() -> pointService.addPoint(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate point");
    }

    // ------------------------------------------------------------
    // 3. DELETE EXISTING POINT
    @Test
    void deletePointByCoordinates_deletesExistingPoint() {
        PointInputDTO input = dto(3, 4);
        Point existing = entity(3, 4);

        when(repository.findByXAndY(3, 4)).thenReturn(Optional.of(existing));

        PointOutputDTO result = pointService.deletePointByCoordinates(input);

        assertThat(result.x()).isEqualTo(3);
        assertThat(result.y()).isEqualTo(4);

        verify(repository).delete(existing);
    }

    // ------------------------------------------------------------
    // 4. DELETE NON-EXISTING POINT
    @Test
    void deletePointByCoordinates_throwsWhenNotFound() {
        PointInputDTO input = dto(3, 4);

        when(repository.findByXAndY(3, 4)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pointService.deletePointByCoordinates(input))
                .isInstanceOf(PointNotFoundException.class);
    }

    // ------------------------------------------------------------
    // 5. IMPORT LIST OF POINTS
    @Test
    void addPoints_savesAllWhenNoDuplicates() {
        List<PointInputDTO> list = List.of(dto(1, 1), dto(2, 2));

        when(repository.existsByXAndY(anyInt(), anyInt())).thenReturn(false);
        when(repository.save(any(Point.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<PointOutputDTO> result = pointService.addPoints(list);

        assertThat(result).hasSize(2);
        verify(repository, times(2)).save(any(Point.class));
    }

    // ------------------------------------------------------------
    // 6. IMPORT LIST WITH DUPLICATES
    @Test
    void addPoints_throwsWhenDuplicatesExist() {
        List<PointInputDTO> list = List.of(dto(1, 1), dto(2, 2));

        when(repository.existsByXAndY(1, 1)).thenReturn(false);
        when(repository.existsByXAndY(2, 2)).thenReturn(true);

        assertThatThrownBy(() -> pointService.addPoints(list))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate points");
    }

    // ------------------------------------------------------------
    // 7. GET ALL POINTS
    @Test
    void getAllPoints_returnsMappedPoints() {
        List<Point> entities = List.of(entity(5, 5), entity(7, 7));

        when(repository.findAll()).thenReturn(entities);

        List<PointOutputDTO> result = pointService.getAllPoints();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("x").containsExactly(5, 7);
        assertThat(result).extracting("y").containsExactly(5, 7);
    }
}