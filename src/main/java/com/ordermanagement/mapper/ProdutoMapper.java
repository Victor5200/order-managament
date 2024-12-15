package com.ordermanagement.mapper;

import com.ordermanagement.model.dto.ProdutoDTO;
import com.ordermanagement.model.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    Produto toEntity(ProdutoDTO produtoDTO);

    ProdutoDTO toDTO(Produto produto);
}
