package br.com.moredevs.pedidos.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PedidoRequest(

        @NotNull(message = "O ID do produto é obrigatório")
        UUID produtoId,

        @Positive(message = "A quantidade deve ser maior que zero")
        int quantidade

) {
}
