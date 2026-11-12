package br.com.moredevs.pedidos.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Produto(
        UUID id,
        String nome,
        BigDecimal preco,
        int estoque
) {
}
