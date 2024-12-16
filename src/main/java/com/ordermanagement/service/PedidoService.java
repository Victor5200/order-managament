package com.ordermanagement.service;

import com.ordermanagement.exception.ResourceNotFoundException;
import com.ordermanagement.mapper.PedidoMapper;
import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.model.dto.PedidoResumoDTO;
import com.ordermanagement.model.entity.Pedido;
import com.ordermanagement.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    private final KafkaTemplate<String, PedidoResumoDTO> kafkaTemplate;

    @KafkaListener(topics = "pedidos-topic", groupId = "pedidos-group")
    public void processarPedidos(PedidoDTO pedidoDTO) {
        try {
            if (!pedidoRepository.existsById(pedidoDTO.getId())) {
                Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
                pedidoRepository.save(pedido);
            }

            BigDecimal valorTotal = calcularValorTotal(pedidoDTO);

            PedidoResumoDTO resumo = PedidoResumoDTO.builder()
                    .id(pedidoDTO.getId())
                    .valorTotal(valorTotal)
                    .build();

            kafkaTemplate.send("pedidos-resumo-topic", resumo);
        } catch (Exception e) {
            log.error("Erro ao processar pedido: " + pedidoDTO.getId(), e);
        }
    }

    public List<PedidoDTO> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(pedidoMapper::toDTO)
                .toList();
    }

    public PedidoDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
        return pedidoMapper.toDTO(pedido);
    }

    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedido);
    }

    public PedidoDTO atualizarPedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
        pedidoExistente.setStatus(pedidoDTO.getStatus());
        pedidoExistente.setValorTotal(calcularValorTotal(pedidoDTO));
        Pedido pedidoAtualizado = pedidoRepository.save(pedidoExistente);
        return pedidoMapper.toDTO(pedidoAtualizado);
    }

    public void deletarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
        pedidoRepository.delete(pedido);
    }

    private BigDecimal calcularValorTotal(PedidoDTO pedidoDTO) {
        return pedidoDTO.getItens().stream()
                .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}