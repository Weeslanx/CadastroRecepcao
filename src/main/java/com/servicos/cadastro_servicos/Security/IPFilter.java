package com.servicos.cadastro_servicos.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

@Component
public class IPFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_IPS = Set.of(
        "192.168.2.60",
        "192.168.2.89"
         // Adicione outros IPs aqui
);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();

        if (!ALLOWED_IPS.contains(clientIp)) {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");

    
    var htmlFile = new ClassPathResource("templates/error.html").getFile();
    var htmlContent = Files.readString(htmlFile.toPath());

    response.getWriter().write(htmlContent);
    return;
}

        filterChain.doFilter(request, response); 
    }
}
