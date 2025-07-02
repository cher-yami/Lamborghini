package com.ccstudent.msventa.controller;

import com.ccstudent.msventa.dto.ProductoDto;
import com.ccstudent.msventa.entity.Venta;
import com.ccstudent.msventa.entity.VentaDetalle;
import com.ccstudent.msventa.feign.ProductoFeign;
import com.ccstudent.msventa.service.PdfService;
import com.ccstudent.msventa.service.VentaService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ProductoFeign productoFeign;

    @PostMapping("/realizar")
    public ResponseEntity<Venta> realizarVenta(@RequestHeader("Authorization") String token) {
        Venta nuevaVenta = ventaService.realizarVenta(token);
        return ResponseEntity.ok(nuevaVenta);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Venta>> listarVentas() {
        List<Venta> ventas = ventaService.listarVentas();
        for (Venta venta : ventas) {
            for (VentaDetalle detalle : venta.getDetalles()) {
                ResponseEntity<ProductoDto> response = productoFeign.listarProducto(detalle.getProductoId());
                if (response.getStatusCode().is2xxSuccessful()) {
                    detalle.setProducto(response.getBody());
                }
            }
        }
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<byte[]> generarRecibo(@PathVariable Integer id) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes;
        try {
            pdfBytes = pdfService.generarReciboPdf(venta);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "recibo_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/registroVentasPdf")
    public ResponseEntity<byte[]> generarRegistroVentasPdf() {
        List<Venta> ventas = ventaService.listarVentas();
        for (Venta venta : ventas) {
            for (VentaDetalle detalle : venta.getDetalles()) {
                ResponseEntity<ProductoDto> response = productoFeign.listarProducto(detalle.getProductoId());
                if (response.getStatusCode().is2xxSuccessful()) {
                    detalle.setProducto(response.getBody());
                }
            }
        }

        byte[] pdfBytes;
        try {
            pdfBytes = pdfService.generarRegistroVentasPdf(ventas);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "registro_ventas.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
