package com.justas.squares_backend.controllers;

import com.justas.squares_backend.dto.SquareDTO;
import com.justas.squares_backend.dto.SquarePointDTO;
import com.justas.squares_backend.services.SquareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SquareController.class)
class SquareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private SquareService squareService;

    // ------------------------------------------------------------
    // 1. GET /squares
    @Test
    void getSquares_returnsList() throws Exception {
        SquareDTO square = new SquareDTO(
                List.of(
                        new SquarePointDTO(0, 0),
                        new SquarePointDTO(0, 2),
                        new SquarePointDTO(2, 0),
                        new SquarePointDTO(2, 2)
                )
        );

        when(squareService.findSquares()).thenReturn(List.of(square));

        mockMvc.perform(get("/squares")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].points[0].x").value(0))
                .andExpect(jsonPath("$[0].points[1].y").value(2));
    }

    // ------------------------------------------------------------
    // 2. GET /squares/count
    @Test
    void getSquareCount_returnsCount() throws Exception {
        when(squareService.countSquares()).thenReturn(5);

        mockMvc.perform(get("/squares/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}