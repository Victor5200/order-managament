package com.ordermanagement.mapper;

import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.model.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    PedidoDTO toDTO(Pedido pedido);

    Pedido toEntity(PedidoDTO pedidoDTO);
}