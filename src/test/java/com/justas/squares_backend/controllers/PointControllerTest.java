package com.justas.squares_backend.controllers;

import com.justas.squares_backend.dto.PointInputDTO;
import com.justas.squares_backend.dto.PointOutputDTO;
import com.justas.squares_backend.services.PointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    // ------------------------------------------------------------
    // 1. POST /points/single
    @Test
    void addPoint_returnsCreatedPoint() throws Exception {
        PointOutputDTO output = new PointOutputDTO(null, 1, 2);

        when(pointService.addPoint(any(PointInputDTO.class))).thenReturn(output);

        mockMvc.perform(post("/points/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"x":1,"y":2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.x").value(1))
                .andExpect(jsonPath("$.y").value(2));
    }

    // ------------------------------------------------------------
    // 2. DELETE /points/single
    @Test
    void deletePoint_returnsNoContent() throws Exception {
        when(pointService.deletePointByCoordinates(any(PointInputDTO.class)))
                .thenReturn(new PointOutputDTO(null, 3, 4));

        mockMvc.perform(delete("/points/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"x":3,"y":4}
                                """))
                .andExpect(status().isNoContent());
    }

    // ------------------------------------------------------------
    // 3. POST /points (import list)
    @Test
    void importPoints_returnsCreatedList() throws Exception {
        List<PointOutputDTO> output = List.of(
                new PointOutputDTO(null, 1, 1),
                new PointOutputDTO(null, 2, 2)
        );

        when(pointService.addPoints(any())).thenReturn(output);

        mockMvc.perform(post("/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [{"x":1,"y":1},{"x":2,"y":2}]
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].x").value(1))
                .andExpect(jsonPath("$[1].y").value(2));
    }

    // ------------------------------------------------------------
    // 4. GET /points
    @Test
    void getAllPoints_returnsList() throws Exception {
        List<PointOutputDTO> output = List.of(
                new PointOutputDTO(null, 5, 5),
                new PointOutputDTO(null, 7, 7)
        );

        when(pointService.getAllPoints()).thenReturn(output);

        mockMvc.perform(get("/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].x").value(5))
                .andExpect(jsonPath("$[1].y").value(7));
    }
}