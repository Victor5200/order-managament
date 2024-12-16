package com.ordermanagement.service;

import com.ordermanagement.exception.ResourceNotFoundException;
import com.ordermanagement.mapper.PedidoMapper;
import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.model.dto.PedidoResumoDTO;
import com.ordermanagement.model.entity.Pedido;
import com.ordermanagement.model.enumerated.PedidoStatusEnum;
import com.ordermanagement.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private KafkaTemplate<String, PedidoResumoDTO> kafkaTemplate;

    @Mock
    private PedidoMapper pedidoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarPedidos() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoMapper.toDTO(pedido)).thenReturn(pedidoDTO);

        List<PedidoDTO> result = pedidoService.listarPedidos();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPedidoPorId() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoMapper.toDTO(pedido)).thenReturn(pedidoDTO);

        PedidoDTO result = pedidoService.buscarPedidoPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPedidoPorIdNotFound() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pedidoService.buscarPedidoPorId(1L));
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testCriarPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        when(pedidoMapper.toEntity(pedidoDTO)).thenReturn(pedido);
        when(pedidoMapper.toDTO(pedido)).thenReturn(pedidoDTO);

        PedidoDTO result = pedidoService.criarPedido(pedidoDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testAtualizarPedido() {
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoExistente));

        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.setId(1L);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoAtualizado);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setStatus(PedidoStatusEnum.EM_ANDAMENTO);
        pedidoDTO.setItens(Arrays.asList());
        when(pedidoMapper.toDTO(pedidoAtualizado)).thenReturn(pedidoDTO);

        PedidoDTO result = pedidoService.atualizarPedido(1L, pedidoDTO);

        assertNotNull(result);
        assertEquals(PedidoStatusEnum.EM_ANDAMENTO, result.getStatus());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testDeletarPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.deletarPedido(1L);

        verify(pedidoRepository, times(1)).delete(pedido);
    }

    @Test
    void testProcessarPedidos() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setItens(Arrays.asList());

        when(pedidoRepository.existsById(1L)).thenReturn(false);

        Pedido pedido = new Pedido();
        when(pedidoMapper.toEntity(pedidoDTO)).thenReturn(pedido);

        PedidoResumoDTO resumo = PedidoResumoDTO.builder()
                .id(1L)
                .valorTotal(BigDecimal.ZERO)
                .build();

        ArgumentCaptor<PedidoResumoDTO> captor = ArgumentCaptor.forClass(PedidoResumoDTO.class);

        pedidoService.processarPedidos(pedidoDTO);

        verify(pedidoRepository, times(1)).save(pedido);
        verify(kafkaTemplate, times(1)).send(eq("pedidos-resumo-topic"), captor.capture());
        assertEquals(1L, captor.getValue().getId());
    }
}
