package com.ccstudent.msproducto.controller;

import com.ccstudent.msproducto.entity.Producto;
import com.ccstudent.msproducto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final Path rootLocation = Paths.get("uploads");

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listar();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> crearProducto(
            @RequestParam("titulo") String titulo,
            @RequestParam("color") String color,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("stock") Integer stock,
            @RequestParam("precio") Double precio,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam("provedoresId") Integer provedoresId,
            @RequestParam("anio") int anio,
            @RequestParam("imagen") MultipartFile imagen) {

        LocalDate fechaAnio = LocalDate.of(anio, 1, 1);
        Producto producto = productoService.guardar(titulo, color, descripcion, stock, precio, categoriaId, provedoresId, imagen, fechaAnio);
        return ResponseEntity.ok(producto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> editarProducto(
            @PathVariable Integer id,
            @RequestParam("titulo") String titulo,
            @RequestParam("color") String color,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("stock") Integer stock,
            @RequestParam("precio") Double precio,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam("provedoresId") Integer provedoresId,
            @RequestParam("anio") int anio,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        LocalDate fechaAnio = LocalDate.of(anio, 1, 1);
        Producto producto = productoService.editar(id, titulo, color, descripcion, stock, precio, categoriaId, provedoresId, imagen, fechaAnio);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Optional<Producto> listarProductoPorId(@PathVariable Integer id) {
        return productoService.listarPorId(id);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Integer id, @RequestParam Integer cantidad) {
        Producto producto = productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> listarPorCategoria(@PathVariable Integer categoriaId) {
        return productoService.listarPorCategoria(categoriaId);
    }

    @GetMapping("/anio/{anio}")
    public List<Producto> listarPorAnio(@PathVariable int anio) {
        return productoService.listarPorAnio(LocalDate.of(anio, 1, 1));
    }

    @GetMapping("/anio/desde/{anio}")
    public List<Producto> listarDesdeAnio(@PathVariable int anio) {
        return productoService.listarDesdeAnio(LocalDate.of(anio, 1, 1));
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }
}
