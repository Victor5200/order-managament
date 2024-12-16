package com.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.model.dto.ProdutoDTO;
import com.ordermanagement.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    private ProdutoDTO produtoDTO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();

        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Produto Teste");
        produtoDTO.setPreco(BigDecimal.valueOf(100.0));
    }

    @Test
    void testListarProdutos() throws Exception {
        when(produtoService.listarProdutos()).thenReturn(List.of(produtoDTO));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(produtoDTO.getId()))
                .andExpect(jsonPath("$[0].nome").value(produtoDTO.getNome()));

        verify(produtoService, times(1)).listarProdutos();
    }

    @Test
    void testBuscarProdutoPorId() throws Exception {
        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produtoDTO);

        mockMvc.perform(get("/api/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(produtoDTO.getId()))
                .andExpect(jsonPath("$.nome").value(produtoDTO.getNome()));

        verify(produtoService, times(1)).buscarProdutoPorId(1L);
    }

    @Test
    void testCriarProduto() throws Exception {
        when(produtoService.criarProduto(any(ProdutoDTO.class))).thenReturn(produtoDTO);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(produtoDTO.getId()))
                .andExpect(jsonPath("$.nome").value(produtoDTO.getNome()));

        verify(produtoService, times(1)).criarProduto(any(ProdutoDTO.class));
    }

    @Test
    void testAtualizarProduto() throws Exception {
        ProdutoDTO produtoAtualizado = new ProdutoDTO();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setPreco(BigDecimal.valueOf(200.0));

        when(produtoService.atualizarProduto(eq(1L), any(ProdutoDTO.class))).thenReturn(produtoAtualizado);

        mockMvc.perform(put("/api/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(produtoAtualizado.getId()))
                .andExpect(jsonPath("$.nome").value(produtoAtualizado.getNome()));

        verify(produtoService, times(1)).atualizarProduto(eq(1L), any(ProdutoDTO.class));
    }

    @Test
    void testDeletarProduto() throws Exception {
        doNothing().when(produtoService).deletarProduto(1L);

        mockMvc.perform(delete("/api/produtos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(produtoService, times(1)).deletarProduto(1L);
    }
}
