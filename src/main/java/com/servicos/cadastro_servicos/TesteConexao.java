package com.servicos.cadastro_servicos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {
        // URL do banco de dados
        String url = "jdbc:mysql://192.168.2.60:3306/BDRECP"; 
        // Seu usuário
        String usuario = "admin"; // Altere para o usuário admin
        // Sua senha
        String senha = "suaSenhaSegura"; // Altere para a senha que você definiu

        try {
            // Tenta estabelecer a conexão
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão estabelecida com sucesso!");

            // Fechar a conexão
            conexao.close();
        } catch (SQLException e) {
            System.out.println("Falha na conexão!");
            e.printStackTrace(); // Exibe a exceção detalhada
        }
    }
}
