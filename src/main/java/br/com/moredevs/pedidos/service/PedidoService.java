package br.com.moredevs.pedidos.service;

import br.com.moredevs.pedidos.model.Produto;
import br.com.moredevs.pedidos.model.dtos.PedidoRequest;
import br.com.moredevs.pedidos.model.dtos.PedidoResponse;
import br.com.moredevs.pedidos.model.dtos.PedidoResponseV2;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PedidoService {

    public static final UUID PEDIDO_1_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    public static final UUID PEDIDO_2_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    public static final UUID PEDIDO_3_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    private final ProdutoService produtoService;

    private final Map<UUID, PedidoResponseV2> pedidos =
            new ConcurrentHashMap<>();

    public PedidoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostConstruct
    public void inicializarPedidosParaTeste() {
        registrarPedidoComIdFixo(PEDIDO_1_ID, ProdutoService.NOTEBOOK_ID, 1, "RECEBIDO");
        registrarPedidoComIdFixo(PEDIDO_2_ID, ProdutoService.MOUSE_ID, 3, "EM_PREPARACAO");
        registrarPedidoComIdFixo(PEDIDO_3_ID, ProdutoService.TECLADO_ID, 2, "ENVIADO");
    }

    private void registrarPedidoComIdFixo(UUID pedidoId, UUID produtoId, int quantidade, String status) {
        Produto produto = produtoService.reservar(produtoId, quantidade);
        BigDecimal subtotal = produto.preco().multiply(BigDecimal.valueOf(quantidade));

        PedidoResponseV2 pedido = new PedidoResponseV2(
                pedidoId,
                status,
                Instant.now(),
                subtotal,
                List.of(
                        new PedidoResponseV2.ItemResponseV2(
                                produto.id(),
                                produto.nome(),
                                quantidade,
                                produto.preco(),
                                subtotal
                        )
                )
        );

        pedidos.put(pedidoId, pedido);
    }

    private PedidoResponseV2 processar(PedidoRequest request) {
        if (request == null || request.produtoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os dados do pedido e o ID do produto são obrigatórios"
            );
        }

        if (request.quantidade() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A quantidade deve ser maior que zero"
            );
        }

        Produto produto = produtoService.reservar(
                request.produtoId(),
                request.quantidade()
        );

        BigDecimal subtotal = produto.preco()
                .multiply(BigDecimal.valueOf(request.quantidade()));

        UUID id = UUID.randomUUID();

        PedidoResponseV2 response = new PedidoResponseV2(
                id,
                "RECEBIDO",
                Instant.now(),
                subtotal,
                List.of(
                        new PedidoResponseV2.ItemResponseV2(
                                produto.id(),
                                produto.nome(),
                                request.quantidade(),
                                produto.preco(),
                                subtotal
                        )
                )
        );

        pedidos.put(id, response);

        return response;
    }

    public PedidoResponse criar(PedidoRequest request) {
        PedidoResponseV2 pedido = processar(request);
        return toV1Response(pedido);
    }

    public PedidoResponseV2 criarV2(PedidoRequest request) {
        return processar(request);
    }

    public List<PedidoResponse> listarTodos() {
        return pedidos.values().stream()
                .map(this::toV1Response)
                .toList();
    }

    public List<PedidoResponseV2> listarTodosV2() {
        return List.copyOf(pedidos.values());
    }

    public PedidoResponse buscarPorId(UUID id) {
        PedidoResponseV2 pedido = buscarPedido(id);
        return toV1Response(pedido);
    }

    public PedidoResponseV2 buscarPorIdV2(UUID id) {
        return buscarPedido(id);
    }

    private PedidoResponseV2 buscarPedido(UUID id) {
        PedidoResponseV2 pedido = pedidos.get(id);

        if (pedido == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pedido não encontrado: " + id
            );
        }

        return pedido;
    }

    private PedidoResponse toV1Response(PedidoResponseV2 pedido) {
        PedidoResponseV2.ItemResponseV2 item = pedido.itens().getFirst();

        return new PedidoResponse(
                pedido.id(),
                item.produtoId(),
                item.nome(),
                item.quantidade(),
                pedido.valorTotal(),
                pedido.statusProcessamento(),
                pedido.dataCriacao()
        );
    }
}
