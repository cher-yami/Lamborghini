package com.ccstudent.msventa.service;

import com.ccstudent.msventa.dto.ProductoDto;
import com.ccstudent.msventa.entity.Venta;
import com.ccstudent.msventa.entity.VentaDetalle;
import com.ccstudent.msventa.feign.ProductoFeign;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private ProductoFeign productoFeign;

    public byte[] generarReciboPdf(Venta venta) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        document.add(new Paragraph("Tienda de Tecnología CODETECH", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("RUC: 12345678901", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Recibo de Venta", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph(" "));

        PdfPTable tableInfo = new PdfPTable(2);
        tableInfo.setWidthPercentage(100);
        tableInfo.setSpacingBefore(10f);
        tableInfo.setSpacingAfter(10f);

        tableInfo.addCell(createCell("ID Venta:"));
        tableInfo.addCell(createCell(String.valueOf(venta.getId())));
        tableInfo.addCell(createCell("Fecha:"));
        tableInfo.addCell(createCell(venta.getFecha().toString()));
        tableInfo.addCell(createCell("Usuario:"));
        tableInfo.addCell(createCell(String.valueOf(venta.getUserName())));
        document.add(tableInfo);

        PdfPTable tableDetalles = new PdfPTable(4);
        tableDetalles.setWidthPercentage(100);
        tableDetalles.setSpacingBefore(10f);
        tableDetalles.setSpacingAfter(10f);
        tableDetalles.setWidths(new float[]{1f, 4f, 1f, 2f});

        tableDetalles.addCell("Item");
        tableDetalles.addCell("Producto");
        tableDetalles.addCell("Cantidad");
        tableDetalles.addCell("Precio");

        int itemCounter = 1;
        for (VentaDetalle detalle : venta.getDetalles()) {
            if (detalle.getProducto() == null) {
                ResponseEntity<ProductoDto> response = productoFeign.listarProducto(detalle.getProductoId());
                if (response.getStatusCode().is2xxSuccessful()) {
                    detalle.setProducto(response.getBody());
                } else {
                    throw new RuntimeException("No se pudo obtener el producto con ID: " + detalle.getProductoId());
                }
            }

            ProductoDto producto = detalle.getProducto();
            tableDetalles.addCell(String.valueOf(itemCounter++));
            tableDetalles.addCell(producto.getTitulo() + " - " + producto.getColor());
            tableDetalles.addCell(String.valueOf(detalle.getCantidad()));
            tableDetalles.addCell(String.format("%.2f", detalle.getPrecio()));
        }

        document.add(tableDetalles);

        PdfPTable tableTotales = new PdfPTable(2);
        tableTotales.setWidthPercentage(100);
        tableTotales.setSpacingBefore(10f);
        tableTotales.setSpacingAfter(10f);
        tableTotales.setWidths(new float[]{8f, 2f});

        tableTotales.addCell(createCell("Total:"));
        tableTotales.addCell(createCell(String.format("%.2f", venta.getTotal())));
        tableTotales.addCell(createCell("IGV:"));
        tableTotales.addCell(createCell(String.format("%.2f", venta.getIgv())));
        tableTotales.addCell(createCell("Total con IGV:"));
        tableTotales.addCell(createCell(String.format("%.2f", venta.getTotalConIgv())));

        document.add(tableTotales);
        document.close();
        return baos.toByteArray();
    }

    public byte[] generarRegistroVentasPdf(List<Venta> ventas) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        document.add(new Paragraph("Tienda de Tecnología CODETECH", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("RUC: 12345678901", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Registro de Ventas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph(" "));

        PdfPTable tableVentas = new PdfPTable(6);
        tableVentas.setWidthPercentage(100);
        tableVentas.setSpacingBefore(10f);
        tableVentas.setSpacingAfter(10f);
        tableVentas.setWidths(new float[]{1f, 2f, 2f, 2f, 2f, 2f});

        tableVentas.addCell("ID Venta");
        tableVentas.addCell("Fecha");
        tableVentas.addCell("Usuario ID");
        tableVentas.addCell("Total");
        tableVentas.addCell("IGV");
        tableVentas.addCell("Total con IGV");

        for (Venta venta : ventas) {
            tableVentas.addCell(String.valueOf(venta.getId()));
            tableVentas.addCell(venta.getFecha().toString());
            tableVentas.addCell(String.valueOf(venta.getUserId()));
            tableVentas.addCell(String.format("%.2f", venta.getTotal()));
            tableVentas.addCell(String.format("%.2f", venta.getIgv()));
            tableVentas.addCell(String.format("%.2f", venta.getTotalConIgv()));

            PdfPTable tableDetalles = new PdfPTable(5);
            tableDetalles.setWidthPercentage(100);
            tableDetalles.setSpacingBefore(5f);
            tableDetalles.setWidths(new float[]{1f, 3f, 2f, 1f, 2f});

            tableDetalles.addCell("Item");
            tableDetalles.addCell("Producto");
            tableDetalles.addCell("Cantidad");
            tableDetalles.addCell("Precio");
            tableDetalles.addCell("Precio con IGV");

            int itemCounter = 1;
            for (VentaDetalle detalle : venta.getDetalles()) {
                if (detalle.getProducto() == null) {
                    ResponseEntity<ProductoDto> response = productoFeign.listarProducto(detalle.getProductoId());
                    if (response.getStatusCode().is2xxSuccessful()) {
                        detalle.setProducto(response.getBody());
                    } else {
                        throw new RuntimeException("No se pudo obtener el producto con ID: " + detalle.getProductoId());
                    }
                }

                ProductoDto producto = detalle.getProducto();
                tableDetalles.addCell(String.valueOf(itemCounter++));
                tableDetalles.addCell(producto.getTitulo() + " - " + producto.getColor());
                tableDetalles.addCell(String.valueOf(detalle.getCantidad()));
                tableDetalles.addCell(String.format("%.2f", detalle.getPrecio()));
                tableDetalles.addCell(String.format("%.2f", detalle.getPrecio() * 1.18));
            }

            PdfPCell detallesCell = new PdfPCell(tableDetalles);
            detallesCell.setColspan(6);
            tableVentas.addCell(detallesCell);
        }

        document.add(tableVentas);
        document.close();
        return baos.toByteArray();
    }

    private PdfPCell createCell(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}