package com.justas.squares_backend.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.justas.squares_backend.dto.SquareDTO;
import com.justas.squares_backend.dto.SquarePointDTO;
import com.justas.squares_backend.entities.Point;
import com.justas.squares_backend.helpers.PointKey;
import com.justas.squares_backend.repository.PointRepository;

@Service
public class SquareService {

    private final PointRepository pointRepository;

    public SquareService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public List<SquareDTO> findSquares() {

        List<Point> points = pointRepository.findAll();

        Set<PointKey> pointSet = points.stream()
                .map(p -> new PointKey(p.getX(), p.getY()))
                .collect(Collectors.toSet());

        List<SquareDTO> squares = new ArrayList<>();
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Point a = points.get(i);

            for (int j = i + 1; j < n; j++) {
                Point c = points.get(j);

                // Skip identical points (zero-length diagonal)
                if (a.getX() == c.getX() && a.getY() == c.getY()) {
                    continue;
                }

                // Midpoint of AC
                double mx = (a.getX() + c.getX()) / 2.0;
                double my = (a.getY() + c.getY()) / 2.0;

                // Vector from midpoint to A
                double vx = a.getX() - mx;
                double vy = a.getY() - my;

                // Rotate 90 degrees to get midpoint→B
                double rx = -vy;
                double ry = vx;

                // Compute B and D
                double bx = mx + rx;
                double by = my + ry;

                double dx = mx - rx;
                double dy = my - ry;

                // B and D must be integer coordinates
                if (!isInteger(bx) || !isInteger(by) || !isInteger(dx) || !isInteger(dy)) {
                    continue;
                }

                PointKey b = new PointKey((int) bx, (int) by);
                PointKey d = new PointKey((int) dx, (int) dy);

                // Check if B and D exist
                if (!pointSet.contains(b) || !pointSet.contains(d)) {
                    continue;
                }

                // Build the four points
                SquarePointDTO p1 = new SquarePointDTO(a.getX(), a.getY());
                SquarePointDTO p2 = new SquarePointDTO(b.x(), b.y());
                SquarePointDTO p3 = new SquarePointDTO(c.getX(), c.getY());
                SquarePointDTO p4 = new SquarePointDTO(d.x(), d.y());

                // --- ORDER POINTS INTO PERIMETER ORDER ---
                List<SquarePointDTO> pts = new ArrayList<>(List.of(p1, p2, p3, p4));

                double cx = (p1.x() + p2.x() + p3.x() + p4.x()) / 4.0;
                double cy = (p1.y() + p2.y() + p3.y() + p4.y()) / 4.0;

                pts.sort(Comparator.comparingDouble(p ->
                        Math.atan2(p.y() - cy, p.x() - cx)
                ));

                SquarePointDTO s1 = pts.get(0);
                SquarePointDTO s2 = pts.get(1);
                SquarePointDTO s3 = pts.get(2);
                SquarePointDTO s4 = pts.get(3);

                // --- VALIDATE SIDES ---
                int d12 = dist2(s1, s2);
                int d23 = dist2(s2, s3);
                int d34 = dist2(s3, s4);
                int d41 = dist2(s4, s1);

                if (d12 == 0 || d12 != d23 || d23 != d34 || d34 != d41) {
                    continue; // not a square
                }

                // --- VALIDATE DIAGONALS ---
                int diag1 = dist2(s1, s3);
                int diag2 = dist2(s2, s4);

                if (diag1 != diag2) {
                    continue; // diagonals must match
                }

                // Normalize for deduplication
                SquareDTO square = normalizeSquare(pts);

                if (!squares.contains(square)) {
                    squares.add(square);
                }
            }
        }

        return squares;
    }

    private boolean isInteger(double v) {
        return Math.floor(v) == v;
    }

    private int dist2(SquarePointDTO a, SquarePointDTO b) {
        int dx = a.x() - b.x();
        int dy = a.y() - b.y();
        return dx * dx + dy * dy;
    }

    private SquareDTO normalizeSquare(List<SquarePointDTO> points) {
        List<SquarePointDTO> sorted = points.stream()
                .sorted(Comparator.comparing(SquarePointDTO::x)
                        .thenComparing(SquarePointDTO::y))
                .toList();

        return new SquareDTO(sorted);
    }

    public int countSquares() {
        return findSquares().size();
    }
}

