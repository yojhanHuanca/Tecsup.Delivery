package com.Tecsup.repository;

import com.Tecsup.model.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCorreo(String correo);

    Optional<Cliente> findByCorreo(String correo);
}
