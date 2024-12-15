package com.ordermanagement.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoProdutoDTO {
    private Long produtoId;
    private Integer quantidade;
    private BigDecimal preco;
}