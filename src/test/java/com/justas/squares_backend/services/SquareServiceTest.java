package com.justas.squares_backend.services;

import com.justas.squares_backend.dto.SquareDTO;
import com.justas.squares_backend.dto.SquarePointDTO;
import com.justas.squares_backend.entities.Point;
import com.justas.squares_backend.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SquareServiceTest {

    private PointRepository repository;
    private SquareService squareService;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(PointRepository.class);
        squareService = new SquareService(repository);
    }

    private Point p(int x, int y) {
        return new Point(x, y);
    }

    // ------------------------------------------------------------
    // 1. SIMPLE AXIS-ALIGNED SQUARE
    @Test
    void detectsSimpleAxisAlignedSquare() {
        List<Point> points = List.of(
                p(-1, -1),
                p(-1, 1),
                p(1, -1),
                p(1, 1)
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).hasSize(1);

        SquareDTO sq = squares.get(0);
        assertThat(sq.points()).containsExactlyInAnyOrder(
                new SquarePointDTO(-1, -1),
                new SquarePointDTO(-1, 1),
                new SquarePointDTO(1, -1),
                new SquarePointDTO(1, 1)
        );
    }

    // ------------------------------------------------------------
    // 2. ROTATED SQUARE (diamond shape)
    @Test
    void detectsRotatedSquare() {
        List<Point> points = List.of(
                p(0, 2),
                p(2, 0),
                p(0, -2),
                p(-2, 0)
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).hasSize(1);
    }

    // ------------------------------------------------------------
    // 3. MULTIPLE SQUARES
    @Test
    void detectsMultipleSquares() {
        List<Point> points = List.of(
                // Square 1
                p(0, 0), p(0, 2), p(2, 0), p(2, 2),
                // Square 2
                p(3, 3), p(3, 5), p(5, 3), p(5, 5)
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).hasSize(2);
    }

    // ------------------------------------------------------------
    // 4. NO SQUARES
    @Test
    void returnsEmptyListWhenNoSquares() {
        List<Point> points = List.of(
                p(0, 0),
                p(1, 2),
                p(5, 7)
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).isEmpty();
    }

    // ------------------------------------------------------------
    // 5. RECTANGLE BUT NOT SQUARE
    @Test
    void doesNotDetectRectangleAsSquare() {
        List<Point> points = List.of(
                p(0, 0),
                p(0, 2),
                p(4, 0),
                p(4, 2) // rectangle 4x2
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).isEmpty();
    }

    // ------------------------------------------------------------
    // 6. RHOMBUS BUT NOT SQUARE
    @Test
    void doesNotDetectRhombusAsSquare() {
        List<Point> points = List.of(
                p(0, 0),
                p(2, 1),
                p(4, 0),
                p(2, -1) // rhombus, diagonals not equal
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).isEmpty();
    }

    // ------------------------------------------------------------
    // 7. DUPLICATE POINTS SHOULD NOT BREAK DETECTION
    @Test
    void ignoresDuplicatePoints() {
        List<Point> points = List.of(
                p(-1, -1),
                p(-1, 1),
                p(1, -1),
                p(1, 1),
                p(1, 1) // duplicate
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).hasSize(1);
    }

    // ------------------------------------------------------------
    // 8. ZERO-LENGTH DIAGONAL SHOULD NOT FORM SQUARE
    @Test
    void zeroLengthDiagonalDoesNotFormSquare() {
        List<Point> points = List.of(
                p(0, 0),
                p(0, 0), // duplicate point
                p(1, 1),
                p(1, -1)
        );

        when(repository.findAll()).thenReturn(points);

        List<SquareDTO> squares = squareService.findSquares();

        assertThat(squares).isEmpty();
    }
}

