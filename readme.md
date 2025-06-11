# SuplementoEAJ

Sistema web para venda de suplementos alimentares, desenvolvido em Java com Spring Boot, Thymeleaf, Spring Security e JPA/Hibernate.

## Funcionalidades

- Cadastro, edição, exclusão lógica (soft-delete) e restauração de suplementos (admin)
- Listagem de suplementos ativos com imagens armazenadas em `/static/img`
- Carrinho de compras por sessão (não persistente), com soma de quantidades por produto (sem duplicidade)
- Cadastro e autenticação de usuários (Spring Security)
- Controle de acesso por perfil (admin/usuário)
- Mensagens de feedback para ações do usuário
