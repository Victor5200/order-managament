package com.ordermanagement.controller;

import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoService pedidoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();
    }

    @Test
    void testListarPedidos() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoService.listarPedidos()).thenReturn(List.of(pedidoDTO));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(pedidoService, times(1)).listarPedidos();
    }

    @Test
    void testBuscarPedidoPorId() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoService.buscarPedidoPorId(1L)).thenReturn(pedidoDTO);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(pedidoService, times(1)).buscarPedidoPorId(1L);
    }

    @Test
    void testCriarPedido() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoService.criarPedido(any(PedidoDTO.class))).thenReturn(pedidoDTO);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(pedidoService, times(1)).criarPedido(any(PedidoDTO.class));
    }

    @Test
    void testAtualizarPedido() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoService.atualizarPedido(eq(1L), any(PedidoDTO.class))).thenReturn(pedidoDTO);

        mockMvc.perform(put("/api/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(pedidoService, times(1)).atualizarPedido(eq(1L), any(PedidoDTO.class));
    }

    @Test
    void testDeletarPedido() throws Exception {
        doNothing().when(pedidoService).deletarPedido(1L);

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isOk());

        verify(pedidoService, times(1)).deletarPedido(1L);
    }
}
