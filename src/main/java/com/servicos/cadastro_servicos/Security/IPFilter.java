package com.servicos.cadastro_servicos.Security;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IPFilter extends OncePerRequestFilter {
 /*
    private static final Set<String> ALLOWED_IPS = Set.of(
        "192.168.2.60",
        "192.168.2.223"
         
);
*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        /* String clientIp = request.getRemoteAddr();
        // Validação de IPs permitidos (comentada para aceitar qualquer IP)
        if (!ALLOWED_IPS.contains(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
    
            var htmlFile = new ClassPathResource("templates/error.html").getFile();
            var htmlContent = Files.readString(htmlFile.toPath());
    
            response.getWriter().write(htmlContent);
            return;
        }
        */
    
        // Permite o processamento contínuo para todos os IPs
        filterChain.doFilter(request, response);
    }

}   