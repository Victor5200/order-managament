package com.ordermanagement.service;

import com.ordermanagement.exception.ResourceNotFoundException;
import com.ordermanagement.mapper.ProdutoMapper;
import com.ordermanagement.model.dto.ProdutoDTO;
import com.ordermanagement.model.entity.Produto;
import com.ordermanagement.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper = ProdutoMapper.INSTANCE;

    public ProdutoDTO criarProduto(ProdutoDTO produtoDTO) {
        Produto produto = produtoMapper.toEntity(produtoDTO);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDTO(produto);
    }

    public List<ProdutoDTO> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(produtoMapper::toDTO)
                .toList();
    }

    public ProdutoDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return produtoMapper.toDTO(produto);
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        produtoExistente.setNome(produtoDTO.getNome());
        produtoExistente.setPreco(produtoDTO.getPreco());

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return produtoMapper.toDTO(produtoAtualizado);
    }

    public void deletarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        produtoRepository.delete(produto);
    }
}
