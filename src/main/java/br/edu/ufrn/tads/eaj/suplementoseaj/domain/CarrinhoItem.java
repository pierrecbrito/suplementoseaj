package br.edu.ufrn.tads.eaj.suplementoseaj.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarrinhoItem {
    private Suplemento suplemento;
    private Integer quantidade = 1;
}