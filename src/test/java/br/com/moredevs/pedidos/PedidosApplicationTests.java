package br.com.moredevs.pedidos;

import br.com.moredevs.pedidos.model.dtos.PedidoRequest;
import br.com.moredevs.pedidos.model.dtos.PedidoResponse;
import br.com.moredevs.pedidos.model.dtos.PedidoResponseV2;
import br.com.moredevs.pedidos.service.PedidoService;
import br.com.moredevs.pedidos.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PedidosApplicationTests {

    @Autowired
    private PedidoService pedidoService;

    @Test
    void deveCarregarPedidosIniciaisParaTeste() {
        PedidoResponse v1 = pedidoService.buscarPorId(PedidoService.PEDIDO_1_ID);
        PedidoResponseV2 v2 = pedidoService.buscarPorIdV2(PedidoService.PEDIDO_1_ID);

        assertEquals(PedidoService.PEDIDO_1_ID, v1.id());
        assertEquals(v1.id(), v2.id());
        assertEquals("RECEBIDO", v2.statusProcessamento());
        assertEquals(1, v2.itens().size());
    }

    @Test
    void deveCriarEBuscarPedidoNasVersoesV1EV2ComUuidUnificado() {
        PedidoRequest request = new PedidoRequest(ProdutoService.MOUSE_ID, 2);

        PedidoResponse v1 = pedidoService.criar(request);
        assertNotNull(v1.id());
        assertEquals(ProdutoService.MOUSE_ID, v1.produtoId());
        assertEquals(2, v1.quantidade());

        PedidoResponseV2 v2 = pedidoService.buscarPorIdV2(v1.id());
        assertEquals(v1.id(), v2.id());
        assertEquals("RECEBIDO", v2.statusProcessamento());
        assertEquals(1, v2.itens().size());
        assertEquals(ProdutoService.MOUSE_ID, v2.itens().getFirst().produtoId());
    }
}
