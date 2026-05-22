package com.Tecsup.service;

import com.Tecsup.dto.ProductoRequestDTO;
import com.Tecsup.dto.ProductoResponseDTO;
import com.Tecsup.model.Categoria;
import com.Tecsup.model.Producto;
import com.Tecsup.exception.ResourceNotFoundException;
import com.Tecsup.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO request) {
        Categoria categoria = categoriaService.obtenerEntidad(request.categoriaId());
        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .precio(request.precio())
                .stock(request.stock())
                .categoria(categoria)
                .build();
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO request) {
        Producto producto = obtenerEntidad(id);
        Categoria categoria = categoriaService.obtenerEntidad(request.categoriaId());
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerEntidad(id);
        productoRepository.delete(producto);
    }

    @Transactional(readOnly = true)
    public Producto obtenerEntidad(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    ProductoResponseDTO toResponse(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        );
    }
}
