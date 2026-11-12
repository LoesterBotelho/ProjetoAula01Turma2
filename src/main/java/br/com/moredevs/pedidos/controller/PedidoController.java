package br.com.moredevs.pedidos.controller;

import br.com.moredevs.pedidos.model.dtos.PedidoRequest;
import br.com.moredevs.pedidos.model.dtos.PedidoResponse;
import br.com.moredevs.pedidos.model.dtos.PedidoResponseV2;
import br.com.moredevs.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping({"/api/v1/pedidos", "/pedidos"})
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criarV1(@Valid @RequestBody PedidoRequest request) {
        return service.criar(request);
    }

    @GetMapping({"/api/v1/pedidos", "/pedidos"})
    public List<PedidoResponse> listarTodosV1() {
        return service.listarTodos();
    }

    @GetMapping({"/api/v1/pedidos/{id}", "/pedidos/{id}"})
    public PedidoResponse buscarPorIdV1(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }
    @PostMapping({"/api/v2/pedidos"})
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseV2 criarV2(@Valid @RequestBody PedidoRequest request) {
        return service.criarV2(request);
    }

    @GetMapping({"/api/v2/pedidos"}) 
    public List<PedidoResponseV2> listarTodosV2() {
        return service.listarTodosV2();
    }

    @GetMapping({"/api/v2/pedidos/{id}"}) 
    public PedidoResponseV2 buscarPorIdV2(@PathVariable UUID id) {
        return service.buscarPorIdV2(id);
    }
    
}
