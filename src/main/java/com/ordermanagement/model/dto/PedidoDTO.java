package com.ordermanagement.model.dto;

import com.ordermanagement.model.enumerated.PedidoStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @Schema(description = "Identificador único do pedido", example = "1")
    private Long id;

    @Schema(description = "Status do pedido", example = "Pendente")
    private PedidoStatusEnum status;

    @Schema(description = "Valor total do pedido", example = "150.00")
    private BigDecimal valorTotal;

    @Schema(description = "Itens incluídos no pedido")
    private List<PedidoProdutoDTO> itens;

    @Schema(description = "Data de criação do pedido", example = "2024-12-16T12:30:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "Número de versão do pedido", example = "1")
    private Integer version;
}
