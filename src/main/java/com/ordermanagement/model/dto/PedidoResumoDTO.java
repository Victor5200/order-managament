package com.ordermanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResumoDTO {
    private Long id;
    private BigDecimal valorTotal;
    private String status;

    private List<ProdutoDTO> produtos;
}
