package com.mpazneb.mpazneb_api.config;

import com.mpazneb.mpazneb_api.entity.Usuario;
import com.mpazneb.mpazneb_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario("admin", sha256("admin123"), "ADMIN"));
            usuarioRepository.save(new Usuario("asistente", sha256("asist123"), "ASISTENTE"));
            usuarioRepository.save(new Usuario("estudiante", sha256("estu123"), "ESTUDIANTE"));
            System.out.println("✅ Usuarios de prueba creados:");
            System.out.println("   - admin / admin123 (ADMIN)");
            System.out.println("   - asistente / asist123 (ASISTENTE)");
            System.out.println("   - estudiante / estu123 (ESTUDIANTE)");
        }
    }

    private String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}