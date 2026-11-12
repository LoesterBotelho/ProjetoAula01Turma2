package br.com.moredevs.pedidos.controller;

import br.com.moredevs.pedidos.model.Produto;
import br.com.moredevs.pedidos.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping({"/api/v1/produtos", "/produtos"}) 
    public List<Produto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping({"/api/v1/produtos/{id}", "/produtos/{id}"})  
    public Produto buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }
}
