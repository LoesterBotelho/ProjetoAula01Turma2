package br.com.moredevs.pedidos.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PedidoResponseV2(
        UUID id,
        @JsonProperty("status_processamento")
        String statusProcessamento,
        @JsonProperty("data_criacao")
        Instant dataCriacao,
        @JsonProperty("valor_total")
        BigDecimal valorTotal,
        List<ItemResponseV2> itens
) {

    public record ItemResponseV2(
            @JsonProperty("produto_id")
            UUID produtoId,
            String nome,
            Integer quantidade,
            @JsonProperty("preco_unitario")
            BigDecimal precoUnitario,
            BigDecimal subtotal
    ) {
    }
}
