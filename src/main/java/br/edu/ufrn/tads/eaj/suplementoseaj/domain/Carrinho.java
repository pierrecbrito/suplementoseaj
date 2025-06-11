package br.edu.ufrn.tads.eaj.suplementoseaj.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Carrinho {
    private List<CarrinhoItem> itens = new ArrayList<>();
    private Double total = 0.0;
}