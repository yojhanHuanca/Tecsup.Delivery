package com.Tecsup.service;

import com.Tecsup.dto.CategoriaRequestDTO;
import com.Tecsup.dto.CategoriaResponseDTO;
import com.Tecsup.model.Categoria;
import com.Tecsup.exception.BusinessException;
import com.Tecsup.exception.ResourceNotFoundException;
import com.Tecsup.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public CategoriaResponseDTO crear(CategoriaRequestDTO request) {
        validarNombreUnico(request.nombre());
        Categoria categoria = Categoria.builder()
                .nombre(request.nombre())
                .build();
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO request) {
        Categoria categoria = obtenerEntidad(id);
        if (!categoria.getNombre().equalsIgnoreCase(request.nombre())) {
            validarNombreUnico(request.nombre());
        }
        categoria.setNombre(request.nombre());
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = obtenerEntidad(id);
        categoriaRepository.delete(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria obtenerEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }

    private void validarNombreUnico(String nombre) {
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessException("La categoria ya existe");
        }
    }

    private CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNombre());
    }
}
