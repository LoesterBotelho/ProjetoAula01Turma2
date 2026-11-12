package br.com.moredevs.pedidos.controller;

import br.com.moredevs.pedidos.model.Produto;
import br.com.moredevs.pedidos.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Produto buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }
}
