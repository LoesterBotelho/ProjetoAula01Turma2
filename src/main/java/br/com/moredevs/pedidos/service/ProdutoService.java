package br.com.moredevs.pedidos.service;

import br.com.moredevs.pedidos.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProdutoService {

    public static final UUID NOTEBOOK_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID MOUSE_ID    = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID TECLADO_ID  = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private final Map<UUID, Produto> produtos = Map.of(
            NOTEBOOK_ID, new Produto(NOTEBOOK_ID, "Notebook", new BigDecimal("3500.00"), 10),
            MOUSE_ID, new Produto(MOUSE_ID, "Mouse", new BigDecimal("80.00"), 50),
            TECLADO_ID, new Produto(TECLADO_ID, "Teclado", new BigDecimal("150.00"), 20)
    );

    private final Map<UUID, Integer> estoques = new ConcurrentHashMap<>();

    public ProdutoService() {
        produtos.forEach((id, produto) ->
                estoques.put(id, produto.estoque())
        );
    }

    public List<Produto> listarTodos() {
        return produtos.keySet().stream()
                .map(this::buscar)
                .toList();
    }

    public Produto buscar(UUID id) {
        Produto produto = produtos.get(id);

        if (produto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado: " + id
            );
        }

        return new Produto(
                produto.id(),
                produto.nome(),
                produto.preco(),
                estoques.get(id)
        );
    }

    public synchronized Produto reservar(UUID id, int quantidade) {
        if (quantidade <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A quantidade deve ser maior que zero"
            );
        }

        Produto produto = buscar(id);

        if (produto.estoque() < quantidade) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estoque insuficiente para o produto: " + produto.nome()
            );
        }

        estoques.put(id, produto.estoque() - quantidade);

        return buscar(id);
    }
}
