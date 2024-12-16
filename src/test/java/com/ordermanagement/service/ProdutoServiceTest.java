package com.ordermanagement.service;

import com.ordermanagement.exception.ResourceNotFoundException;
import com.ordermanagement.mapper.ProdutoMapper;
import com.ordermanagement.model.dto.ProdutoDTO;
import com.ordermanagement.model.entity.Produto;
import com.ordermanagement.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        when(produtoMapper.toEntity(produtoDTO)).thenReturn(produto);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);

        ProdutoDTO result = produtoService.criarProduto(produtoDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testListarProdutos() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);

        List<ProdutoDTO> result = produtoService.listarProdutos();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);

        ProdutoDTO result = produtoService.buscarProdutoPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarProdutoPorIdNotFound() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.buscarProdutoPorId(1L));
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testAtualizarProduto() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Produto Atualizado");
        produtoDTO.setPreco(BigDecimal.valueOf(100.0));
        when(produtoMapper.toDTO(produtoAtualizado)).thenReturn(produtoDTO);

        ProdutoDTO result = produtoService.atualizarProduto(1L, produtoDTO);

        assertNotNull(result);
        assertEquals("Produto Atualizado", result.getNome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testDeletarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.deletarProduto(1L);

        verify(produtoRepository, times(1)).delete(produto);
    }
}
