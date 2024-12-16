package com.ordermanagement.controller;

import com.ordermanagement.model.dto.PedidoDTO;
import com.ordermanagement.service.PedidoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoDTO> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public PedidoDTO buscarPedidoPorId(@PathVariable Long id) {
        return pedidoService.buscarPedidoPorId(id);
    }

    @PostMapping
    public PedidoDTO criarPedido(@RequestBody PedidoDTO pedidoDTO) {
        return pedidoService.criarPedido(pedidoDTO);
    }

    @PutMapping("/{id}")
    public PedidoDTO atualizarPedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        return pedidoService.atualizarPedido(id, pedidoDTO);
    }

    @DeleteMapping("/{id}")
    public void deletarPedido(@PathVariable Long id) {
        pedidoService.deletarPedido(id);
    }
}