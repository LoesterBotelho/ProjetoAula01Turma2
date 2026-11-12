package br.com.moredevs.pedidos.controller;

import br.com.moredevs.pedidos.model.dtos.PedidoRequest;
import br.com.moredevs.pedidos.model.dtos.PedidoResponseV2;
import br.com.moredevs.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/pedidos")
public class PedidoControllerV2 {

    private final PedidoService service;

    public PedidoControllerV2(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseV2 criar(@Valid @RequestBody PedidoRequest request) {
        return service.criarV2(request);
    }

    @GetMapping
    public List<PedidoResponseV2> listarTodos() {
        return service.listarTodosV2();
    }

    @GetMapping("/{id}")
    public PedidoResponseV2 buscarPorId(@PathVariable UUID id) {
        return service.buscarPorIdV2(id);
    }
}
