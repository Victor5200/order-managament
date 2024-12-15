package com.ordermanagement.mapper;

import org.mapstruct.Mapper;


import com.ordermanagement.model.dto.PedidoProdutoDTO;
import com.ordermanagement.model.entity.PedidoProduto;

@Mapper
public interface PedidoProdutoMapper {
    PedidoProdutoDTO toDTO(PedidoProduto pedidoProduto);

    PedidoProduto toEntity(PedidoProdutoDTO itemPedidoDTO);
}