package com.Tecsup.service;

import com.Tecsup.dto.ClienteRequestDTO;
import com.Tecsup.dto.ClienteResponseDTO;
import com.Tecsup.model.Cliente;
import com.Tecsup.exception.BusinessException;
import com.Tecsup.exception.ResourceNotFoundException;
import com.Tecsup.repository.ClienteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO request) {
        validarCorreoUnico(request.correo(), null);
        Cliente cliente = Cliente.builder()
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .correo(request.correo())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .build();
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO request) {
        Cliente cliente = obtenerEntidad(id);
        validarCorreoUnico(request.correo(), id);
        cliente.setNombres(request.nombres());
        cliente.setApellidos(request.apellidos());
        cliente.setCorreo(request.correo());
        cliente.setTelefono(request.telefono());
        cliente.setDireccion(request.direccion());
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerEntidad(id);
        clienteRepository.delete(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente obtenerEntidad(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    private void validarCorreoUnico(String correo, Long idActual) {
        clienteRepository.findByCorreo(correo).ifPresent(cliente -> {
            if (idActual == null || !cliente.getId().equals(idActual)) {
                throw new BusinessException("El correo ya esta registrado");
            }
        });
    }

    private ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cliente.getDireccion()
        );
    }
}
