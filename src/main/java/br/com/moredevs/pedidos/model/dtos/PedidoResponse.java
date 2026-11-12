package br.com.moredevs.pedidos.model.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PedidoResponse(
        UUID id,
        UUID produtoId,
        String produto,
        int quantidade,
        BigDecimal valorTotal,
        String status,
        Instant criadoEm
) {
}
