package com.ordermanagement.mapper;

import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.model.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(target = "itens", source = "itens")
    PedidoDTO toDTO(Pedido pedido);

    @Mapping(target = "itens", source = "itens")
    Pedido toEntity(PedidoDTO pedidoDTO);
}