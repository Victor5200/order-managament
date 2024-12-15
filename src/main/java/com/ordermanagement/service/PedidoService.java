package com.ordermanagement.service;

import com.ordermanagement.exception.ResourceNotFoundException;
import com.ordermanagement.mapper.PedidoMapper;
import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.model.dto.PedidoResumoDTO;
import com.ordermanagement.model.entity.Pedido;
import com.ordermanagement.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final KafkaTemplate<String, PedidoResumoDTO> kafkaTemplate;

    @KafkaListener(topics = "pedidos-topic", groupId = "pedidos-group")
    public void processarPedidos(PedidoDTO pedidoDTO) {
        if (!pedidoRepository.existsById(pedidoDTO.getId())) {
            Pedido pedido = PedidoMapper.INSTANCE.toEntity(pedidoDTO);
            pedidoRepository.save(pedido);
        }

        BigDecimal valorTotal = calcularValorTotal(pedidoDTO);

        PedidoResumoDTO resumo = PedidoResumoDTO.builder()
                .id(pedidoDTO.getId())
                .valorTotal(valorTotal)
                .build();

        // Envia a mensagem para o tópico "pedidos-resumo-topic"
        kafkaTemplate.send("pedidos-resumo-topic", resumo);
    }

    public List<PedidoDTO> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(PedidoMapper.INSTANCE::toDTO)
                .toList();
    }

    public PedidoDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
        return PedidoMapper.INSTANCE.toDTO(pedido);
    }

    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
        Pedido pedido = PedidoMapper.INSTANCE.toEntity(pedidoDTO);
        pedido = pedidoRepository.save(pedido);
        return PedidoMapper.INSTANCE.toDTO(pedido);
    }

    public PedidoDTO atualizarPedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
        pedidoExistente.setStatus(pedidoDTO.getStatus());
        pedidoExistente.setValorTotal(calcularValorTotal(pedidoDTO));
        Pedido pedidoAtualizado = pedidoRepository.save(pedidoExistente);
        return PedidoMapper.INSTANCE.toDTO(pedidoAtualizado);
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