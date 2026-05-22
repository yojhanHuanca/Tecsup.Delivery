package com.Tecsup.service;

import com.Tecsup.dto.DetallePedidoResponseDTO;
import com.Tecsup.dto.PedidoRequestDTO;
import com.Tecsup.dto.PedidoResponseDTO;
import com.Tecsup.model.Cliente;
import com.Tecsup.model.DetallePedido;
import com.Tecsup.model.Pedido;
import com.Tecsup.model.Producto;
import com.Tecsup.exception.ResourceNotFoundException;
import com.Tecsup.exception.StockInsuficienteException;
import com.Tecsup.repository.PedidoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listar() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO request) {
        Cliente cliente = clienteService.obtenerEntidad(request.clienteId());
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .fecha(LocalDate.now())
                .total(BigDecimal.ZERO)
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (var detalleRequest : request.detalles()) {
            Producto producto = productoService.obtenerEntidad(detalleRequest.productoId());
            validarStock(producto, detalleRequest.cantidad());

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(detalleRequest.cantidad()));

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(detalleRequest.cantidad())
                    .subtotal(subtotal)
                    .build();

            producto.setStock(producto.getStock() - detalleRequest.cantidad());
            pedido.getDetalles().add(detalle);
            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminar(Long id) {
        Pedido pedido = obtenerEntidad(id);
        pedidoRepository.delete(pedido);
    }

    @Transactional(readOnly = true)
    public Pedido obtenerEntidad(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    private void validarStock(Producto producto, Integer cantidad) {
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el producto: " + producto.getNombre()
            );
        }
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        List<DetallePedidoResponseDTO> detalles = pedido.getDetalles().stream()
                .map(detalle -> new DetallePedidoResponseDTO(
                        detalle.getId(),
                        detalle.getProducto().getId(),
                        detalle.getProducto().getNombre(),
                        detalle.getProducto().getPrecio(),
                        detalle.getCantidad(),
                        detalle.getSubtotal()
                ))
                .toList();

        String clienteNombreCompleto = pedido.getCliente().getNombres()
                + " "
                + pedido.getCliente().getApellidos();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getTotal(),
                pedido.getCliente().getId(),
                clienteNombreCompleto,
                detalles
        );
    }
}
