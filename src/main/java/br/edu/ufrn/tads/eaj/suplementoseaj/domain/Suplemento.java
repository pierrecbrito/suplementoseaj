package br.edu.ufrn.tads.eaj.suplementoseaj.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suplementos")
public class Suplemento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do suplemento é obrigatório.")
    private String nome;

    @NotBlank(message = "A marca é obrigatória.")
    private String marca;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser positivo.")
    private Double preco;

    @NotBlank(message = "A categoria é obrigatória.")
    private String categoria;

    @NotNull(message = "O peso é obrigatório.")
    @Positive(message = "O peso deve ser positivo.")
    private Double peso;

    @Column(name = "imagem_uri")
    private String imagemUri;

    @Column(name = "is_deleted")
    private Date isDeleted;
}