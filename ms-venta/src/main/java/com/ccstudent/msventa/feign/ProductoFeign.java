package com.ccstudent.msventa.feign;

import com.ccstudent.msventa.dto.ProductoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="ms-producto-service")
public interface ProductoFeign {
    @CircuitBreaker(name = "productoListarPorIdCB", fallbackMethod = "fallbackProducto")
    @GetMapping("/productos/{id}")
    ResponseEntity<ProductoDto> listarProducto(@PathVariable(required = true) Integer id);
    @PutMapping("/productos/{id}/stock")
    ResponseEntity<Void> actualizarStock(@PathVariable Integer id, @RequestParam Integer cantidad);
    default ResponseEntity<ProductoDto> fallbackProducto(Integer id, Exception e) {
        ProductoDto productoDto = new ProductoDto();
        productoDto.setId(9000000);
        return ResponseEntity.ok(productoDto);
    }

}