package com.ordermanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private String status;
    private BigDecimal valorTotal;
    private List<PedidoProdutoDTO> itens;
    private LocalDateTime dataCriacao;
    private Integer version;
}