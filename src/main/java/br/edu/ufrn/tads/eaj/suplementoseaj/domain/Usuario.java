package br.edu.ufrn.tads.eaj.suplementoseaj.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
    @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @NotBlank(message = "O nome é obrigatório")
 private String nome;
 
 @NotBlank(message = "O email é obrigatório")
 @Column(unique = true)
 private String email;
 
 @NotBlank(message = "A senha é obrigatória")
 private String senha;
 
 @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
 private Carrinho carrinho;
}