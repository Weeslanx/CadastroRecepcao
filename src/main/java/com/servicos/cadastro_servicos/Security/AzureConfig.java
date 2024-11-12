package com.servicos.cadastro_servicos.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AzureConfig {

    @Value("${azure.tenant.id}")
    private String tenantId;

    @Value("${azure.client.id}")
    private String clientId;

    @Value("${azure.client.secret}")
    private String clientSecret;

    // Getters para acessar as variáveis em outras partes do código, se necessário
    public String getTenantId() {
        return tenantId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
