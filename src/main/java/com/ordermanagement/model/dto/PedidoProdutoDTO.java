package com.ordermanagement.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoProdutoDTO {

    @Schema(description = "Identificador único do produto no pedido", example = "1")
    private Long produtoId;

    @Schema(description = "Quantidade de produtos no pedido", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto", example = "25.50")
    private BigDecimal preco;
}
