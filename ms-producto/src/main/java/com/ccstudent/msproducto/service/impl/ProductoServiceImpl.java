package com.ccstudent.msproducto.service.impl;

import com.ccstudent.msproducto.entity.Categoria;
import com.ccstudent.msproducto.entity.Producto;
import com.ccstudent.msproducto.entity.Provedores;
import com.ccstudent.msproducto.repository.CategoriaRepository;
import com.ccstudent.msproducto.repository.ProductoRepository;
import com.ccstudent.msproducto.repository.ProvedoresRepository;
import com.ccstudent.msproducto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProvedoresRepository provedoresRepository;

    private final Path rootLocation = Paths.get("uploads");

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(String titulo, String color, String descripcion, Integer stock, Double precio, Integer categoriaId, Integer provedoresId, MultipartFile file, LocalDate anio) {
        String imagenUrl = storeFile(file);
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Provedores provedores = provedoresRepository.findById(provedoresId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Producto producto = new Producto();
        producto.setTitulo(titulo);
        producto.setColor(color);
        producto.setDescripcion(descripcion);
        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setProvedores(provedores);
        producto.setImagenUrl(imagenUrl);
        producto.setAnio(anio);
        return productoRepository.save(producto);
    }

    @Override
    public Producto editar(Integer id, String titulo, String color, String descripcion, Integer stock, Double precio, Integer categoriaId, Integer provedoresId, MultipartFile file, LocalDate anio) {
        Producto productoExistente = productoRepository.findById(id).orElse(null);
        if (productoExistente == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        if (file != null && !file.isEmpty()) {
            deleteFile(productoExistente.getImagenUrl());
            String imagenUrl = storeFile(file);
            productoExistente.setImagenUrl(imagenUrl);
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Provedores provedores = provedoresRepository.findById(provedoresId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        productoExistente.setTitulo(titulo);
        productoExistente.setColor(color);
        productoExistente.setDescripcion(descripcion);
        productoExistente.setStock(stock);
        productoExistente.setPrecio(precio);
        productoExistente.setCategoria(categoria);
        productoExistente.setProvedores(provedores);
        productoExistente.setAnio(anio);
        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminar(Integer id) {
        Producto productoExistente = productoRepository.findById(id).orElse(null);
        if (productoExistente != null) {
            deleteFile(productoExistente.getImagenUrl());
            productoRepository.deleteById(id);
        }
    }

    @Override
    public Optional<Producto> listarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizarStock(Integer id, Integer cantidad) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setStock(cantidad);
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    @Override
    public List<Producto> listarPorCategoria(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Producto> listarPorAnio(LocalDate anio) {
        return productoRepository.findByAnio(anio);
    }

    @Override
    public List<Producto> listarDesdeAnio(LocalDate anio) {
        return productoRepository.findByAnioAfter(anio);
    }

    private String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isValidImageFile(fileExtension)) {
                throw new RuntimeException("Invalid file type. Only JPG, PNG and JPEG are allowed.");
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    private void deleteFile(String fileName) {
        try {
            Path filePath = rootLocation.resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file.", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    private boolean isValidImageFile(String fileExtension) {
        return "jpg".equalsIgnoreCase(fileExtension) ||
                "png".equalsIgnoreCase(fileExtension) ||
                "jpeg".equalsIgnoreCase(fileExtension);
    }
}
