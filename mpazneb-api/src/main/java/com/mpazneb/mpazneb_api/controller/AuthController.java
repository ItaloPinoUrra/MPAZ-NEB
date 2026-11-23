package com.mpazneb.mpazneb_api.controller;

import com.mpazneb.mpazneb_api.entity.Usuario;
import com.mpazneb.mpazneb_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String alias = body.get("alias");
        String clave = body.get("clave");

        if (alias == null || clave == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Alias y clave son obligatorios"));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByAlias(alias);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        Usuario usuario = usuarioOpt.get();
        String hashIngresado = sha256(clave);

        if (!usuario.getClave().equals(hashIngresado)) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        if (!"ACTIVO".equals(usuario.getEstado())) {
            return ResponseEntity.status(403).body(Map.of("error", "Cuenta inactiva"));
        }

        // Respuesta exitosa (token simple por MVP)
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", usuario.getId());
        respuesta.put("alias", usuario.getAlias());
        respuesta.put("rol", usuario.getRol());
        respuesta.put("token", "token-" + usuario.getId() + "-" + System.currentTimeMillis());

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        String alias = body.get("alias");
        String clave = body.get("clave");
        String rol = body.get("rol");

        if (alias == null || alias.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Alias y clave son obligatorios"));
        }

        if (usuarioRepository.findByAlias(alias.trim()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El alias ya se encuentra registrado"));
        }

        String rolFinal = (rol != null && !rol.trim().isEmpty()) ? rol.trim().toUpperCase() : "ESTUDIANTE";
        if (!rolFinal.equals("ADMIN") && !rolFinal.equals("ASISTENTE") && !rolFinal.equals("ESTUDIANTE")) {
            rolFinal = "ESTUDIANTE";
        }

        Usuario nuevoUsuario = new Usuario(alias.trim(), sha256(clave), rolFinal);
        usuarioRepository.save(nuevoUsuario);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario registrado exitosamente");
        respuesta.put("id", nuevoUsuario.getId());
        respuesta.put("alias", nuevoUsuario.getAlias());
        respuesta.put("rol", nuevoUsuario.getRol());

        return ResponseEntity.status(201).body(respuesta);
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hasheando", e);
        }
    }
}