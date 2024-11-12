
package com.servicos.cadastro_servicos.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        model.addAttribute("users", userRepository.findAll());
        return "cadastroUsers"; 
    }
    @ResponseBody
    @GetMapping("/userContagem")
    public Map<String, Object> contarUsuariosEListar() {
        List<User> users = userRepository.findAll();

        // Contando os usuários
        int totalUsuarios = users.size();

        // Criando uma lista de objetos com id, nome e role dos usuários
        List<Map<String, Object>> usuariosInfo = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> usuarioInfo = new HashMap<>();
            usuarioInfo.put("id", user.getId());  // Incluindo o ID do usuário
            usuarioInfo.put("nome", user.getName());
            usuarioInfo.put("role", user.getRole());  // Ou user.getRole().getNome() se for um relacionamento com a classe Role
            usuariosInfo.add(usuarioInfo);
        }

        // Criando o mapa com os dados que vamos retornar
        Map<String, Object> response = new HashMap<>();
        response.put("totalUsuarios", totalUsuarios);
        response.put("usuarios", usuariosInfo);

        return response;  // Retorna o total e os dados dos usuários com o ID
    }


    @PostMapping("/register")
    public String register(@Valid ResterDTO data, Model model) {
        if (userRepository.findByName(data.name()) != null) {
            model.addAttribute("error", "Usuário já existe.");
            model.addAttribute("users", userRepository.findAll());
            return "cadastroUsers";
        }

        String encryptedPassword = passwordEncoder.encode(data.pass());
        User newUser = new User(data.name(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return "redirect:/users/cadastroUsers";
    }

    

    


    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, Model model) {
        if (userRepository.findByName(user.getName()) != null) {
            model.addAttribute("error", "O usuário já existe."); // Adiciona uma mensagem de erro ao modelo
            return "cadastroUsers"; 
        }
    
        
        String encryptedPassword = passwordEncoder.encode(user.getPass());
        User userToSave = new User(user.getName(), encryptedPassword, user.getRole());
    
       
        userRepository.save(userToSave);
    
        
        return "redirect:/users/cadastroUsers";
    }
    
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam String userId, Model model) {
        try {
            UUID uuid = UUID.fromString(userId); // Converte o ID para UUID
            Optional<User> user = userRepository.findById(uuid);
            if (user.isPresent()) {
                userRepository.delete(user.get());
                model.addAttribute("message", "Usuário deletado com sucesso.");
            } else {
                model.addAttribute("error", "Usuário não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "ID de usuário inválido.");
        }
        return "redirect:/users/userContagem";
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
