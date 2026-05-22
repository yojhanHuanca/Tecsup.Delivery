package com.Tecsup.util;

import com.Tecsup.model.Rol;
import com.Tecsup.model.Usuario;
import com.Tecsup.repository.RolRepository;
import com.Tecsup.repository.UsuarioRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Rol adminRole = crearRolSiNoExiste("ROLE_ADMIN");
        Rol userRole = crearRolSiNoExiste("ROLE_USER");

        crearUsuarioSiNoExiste("admin", "admin123", Set.of(adminRole));
        crearUsuarioSiNoExiste("user", "user123", Set.of(userRole));
    }

    private Rol crearRolSiNoExiste(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre(nombre).build()));
    }

    private void crearUsuarioSiNoExiste(String username, String password, Set<Rol> roles) {
        if (!usuarioRepository.existsByUsername(username)) {
            Usuario usuario = Usuario.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .enabled(true)
                    .roles(roles)
                    .build();
            usuarioRepository.save(usuario);
        }
    }
}
