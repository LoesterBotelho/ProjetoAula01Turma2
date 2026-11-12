package br.com.moredevs.pedidos.controller;

import br.com.moredevs.pedidos.model.dtos.PedidoRequest;
import br.com.moredevs.pedidos.model.dtos.PedidoResponse;
import br.com.moredevs.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest request) {
        return service.criar(request);
    }

    @GetMapping
    public List<PedidoResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }
}
