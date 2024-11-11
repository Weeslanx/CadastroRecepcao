
package com.servicos.cadastro_servicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicos.cadastro_servicos.dto.ResterDTO;
import com.servicos.cadastro_servicos.model.AzureUser;
import com.servicos.cadastro_servicos.model.User;
import com.servicos.cadastro_servicos.repository.UserRepository;
import com.servicos.cadastro_servicos.service.AzureUserFetcher;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AzureUserFetcher azureUserFetcher;

    @GetMapping("/cadastroUsers")
    public String showCadastroUsersPage(Model model) {
        model.addAttribute("users", userRepository.findAll()); // Passa a lista de usuários para a página
        return "cadastroUsers"; // Nome da página HTML
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid ResterDTO data, Model model) {
        if (userRepository.findByName(data.name()) != null) {
            model.addAttribute("error", "Usuário já existe.");
            model.addAttribute("users", userRepository.findAll()); // Atualiza a lista de usuários na página
            return "cadastroUsers"; // Retorna para a página de cadastro com erro
        }

        String encryptedPassword = passwordEncoder.encode(data.pass());
        User newUser = new User(data.name(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return "redirect:/users/cadastroUsers"; // Redireciona para a página de listagem após o cadastro
    }
    

    


    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, Model model) {
        if (userRepository.findByName(user.getName()) != null) {
            model.addAttribute("error", "O usuário já existe."); // Adiciona uma mensagem de erro ao modelo
            return "cadastroUsers"; // Retorna para a página atual para exibir o erro
        }
    
        // Usa o passwordEncoder para criptografar a senha
        String encryptedPassword = passwordEncoder.encode(user.getPass());
        User userToSave = new User(user.getName(), encryptedPassword, user.getRole());
    
        // Salva o novo usuário com a senha criptografada
        userRepository.save(userToSave);
    
        // Redireciona para a página desejada após o registro
        return "redirect:/users/cadastroUsers";
    }
    

    @GetMapping("/api/users")
    @ResponseBody
    public List<AzureUser> getUsers() {
        try {
            String accessToken = azureUserFetcher.getAccessToken();
            if (accessToken != null) {
                return azureUserFetcher.fetchUsers(accessToken);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários da Azure API", e);
        }
        return List.of();
    }
    

}
