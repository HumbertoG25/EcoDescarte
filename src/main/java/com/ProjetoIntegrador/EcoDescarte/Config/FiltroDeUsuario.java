package com.ProjetoIntegrador.EcoDescarte.Config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.ProjetoIntegrador.EcoDescarte.models.Usuario;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class FiltroDeUsuario implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI().substring(contextPath.length());

        // Ignora recursos internos do JSF (CSS, JS, imagens, etc.)
        if (uri.startsWith("/jakarta.faces.resource/") || uri.startsWith("/javax.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Usuario usuarioLogado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        boolean ehAdmin = usuarioLogado != null && "ROLE_ADMIN".equalsIgnoreCase(usuarioLogado.getRole());

        // 1. Protege todas as páginas dentro de /admin/
        if (uri.startsWith("/admin/")) {
            if (usuarioLogado == null) {
                res.sendRedirect(contextPath + "/login.xhtml");
                return;
            }
            if (!ehAdmin) {
                res.sendRedirect(contextPath + "/index.xhtml");
                return;
            }
        }

        // 2. Se já estiver logado e acessar /login.xhtml ou /cadastro.xhtml, redireciona conforme o tipo de usuário
        if (usuarioLogado != null && (uri.equals("/login.xhtml") || uri.equals("/cadastro.xhtml"))) {
            if (ehAdmin) {
                res.sendRedirect(contextPath + "/admin/painel.xhtml");
            } else {
                res.sendRedirect(contextPath + "/index.xhtml");
            }
            return;
        }

        chain.doFilter(request, response);
    }
}
