package com.ordermanagement.mapper;

import com.ordermanagement.model.dto.PedidoProdutoDTO;
import com.ordermanagement.model.entity.PedidoProduto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PedidoProdutoMapper {
    PedidoProdutoMapper INSTANCE = Mappers.getMapper(PedidoProdutoMapper.class);
    PedidoProdutoDTO toDTO(PedidoProduto pedidoProduto);

    PedidoProduto toEntity(PedidoProdutoDTO itemPedidoDTO);
}