# ♻️ EcoDescarte

Sistema web para **descarte consciente de lixo eletrônico** (alinhado às **ODS 12 e 13** da ONU). O sistema orienta o usuário com um **checklist de segurança** antes do descarte (como backup e limpeza de dados pessoais) e, após a conclusão dos passos, exibe os **pontos de coleta** que aceitam o equipamento selecionado com botão direto para traçar a rota no Google Maps.

---

## 🌐 Acesso Online (Render)

A aplicação está hospedada no Render e pode ser acessada diretamente pelo link:

- **Login:** https://ecodescarte-fj9w.onrender.com/login.xhtml

> **Nota:** por estar hospedado no plano gratuito do Render, o primeiro acesso após um período de inatividade pode levar cerca de 1 minuto para inicializar o servidor.

---

## 👤 Usuários de Teste

Os usuários abaixo estão pré-cadastrados no script inicial do banco ([bkp_Script-banco-ecodescarte.sql](bkp_Script-banco-ecodescarte.sql)):

| Perfil | Nome | E-mail | Senha |
|---|---|---|---|
| **Administrador** (`ROLE_ADMIN`) | Administrador EcoDescarte | `admin@ecodescarte.com` | `admin123` |
| **Usuário Comum** (`ROLE_USER`) | Maria Silva | `maria@email.com` | `123456` |
| **Usuário Comum** (`ROLE_USER`) | João Souza | `joao@email.com` | `123456` |

*(Também é possível criar novas contas comuns diretamente pela tela de cadastro.)*

---

## ✨ Funcionalidades

### Usuário Comum
- Cadastro e autenticação por e-mail e senha.
- Seleção do tipo de lixo eletrônico desejado.
- Checklist interativo com dicas de segurança e aviso de limpeza de dados pessoais.
- Liberação automática dos **Pontos de Coleta** que aceitam o equipamento após marcar todos os passos do checklist.
- Link direto para **traçar rota no Google Maps** até o ecoponto escolhido.

### Administrador (Central de Gerenciamento — `/admin/painel.xhtml`)
- Acesso protegido por filtro de autenticação (`FiltroDeUsuario`) exclusivo para `ROLE_ADMIN`.
- **Usuários e Admins:** cadastrar novos administradores e listar/remover usuários.
- **Equipamentos e Checklists:** cadastrar novos equipamentos junto com seus passos de descarte e remover equipamentos.
- **Pontos de Coleta:** cadastrar ecopontos, vincular os equipamentos aceitos e listar/remover pontos.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4.1.2** (WebMVC, JDBC, Data JPA)
- **JoinFaces 6.1.2** (Jakarta Faces / Mojarra + Facelets)
- **PostgreSQL**
- **Docker** (multi-stage build) & **Docker Compose**
- **Render** (hospedagem da aplicação e do banco de dados)

---

## 🚀 Como Executar Localmente

### Opção 1 — Com Docker Compose (recomendado)
Sobe automaticamente o PostgreSQL (já populado com o script inicial) e a aplicação:

```bash
docker compose up --build
```

Depois acesse: `http://localhost:8080/login.xhtml`

### Opção 2 — Com Maven e PostgreSQL local
1. Crie o banco de dados no PostgreSQL:
   ```sql
   CREATE DATABASE ecodescarte;
   ```
2. Execute o script [bkp_Script-banco-ecodescarte.sql](bkp_Script-banco-ecodescarte.sql) dentro do banco `ecodescarte`.
3. Configure as variáveis de ambiente (ou ajuste `src/main/resources/application.properties`):
   - `DB_URL` (padrão: `jdbc:postgresql://localhost:5432/ecodescarte`)
   - `DB_USERNAME` (padrão: `postgres`)
   - `DB_PASSWORD` (senha do seu PostgreSQL)
   - `PORT` (padrão: `8080`)
4. Inicie a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(No Windows PowerShell: `.\mvnw.cmd spring-boot:run`)*

5. Acesse no navegador: `http://localhost:8080/login.xhtml`

---

## 📁 Estrutura Principal do Projeto

```text
EcoDescarte/
├── src/main/
│   ├── java/com/ProjetoIntegrador/EcoDescarte/
│   │   ├── Config/         # Filtro de autenticação/autorização (FiltroDeUsuario)
│   │   ├── Controlers/     # Managed Beans JSF (UsuarioBean, CheckListBean, Admin*Bean)
│   │   ├── models/         # Entidades (Usuario, Equipamento, CheckList, PontoDeColeta)
│   │   ├── Repositorios/   # Acesso ao PostgreSQL via JdbcTemplate
│   │   └── Services/       # Regras de negócio
│   └── resources/
│       ├── application.properties
│       └── META-INF/resources/
│           ├── index.xhtml, login.xhtml, cadastro.xhtml
│           ├── admin/      # template.xhtml, painel.xhtml, usuarios.xhtml, equipamentos.xhtml, pontos.xhtml
│           └── resources/css/style.css
├── bkp_Script-banco-ecodescarte.sql
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```
