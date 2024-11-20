package dss.pvalenz23.practica1.servicios;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dss.pvalenz23.practica1.modelos.Producto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioPDF {

    public byte[] generarPdfCompra(List<Producto> lista) throws FileNotFoundException, DocumentException {
        
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        // Titulo del Pdf
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("La tienda de pValenz", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));

        // Tabla de productos
        PdfPTable table = new PdfPTable(2); 
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 1}); 
        table.setSpacingBefore(10f); 
        table.setSpacingAfter(10f); 

        // Estilos de fuente
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font fontProducto = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font fontSubtotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

        // Encabezados de tabla
        PdfPCell headerNombre = new PdfPCell(new Phrase("Producto", fontHeader));
        PdfPCell headerPrecio = new PdfPCell(new Phrase("Precio", fontHeader));
        headerNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerPrecio.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerNombre.setBackgroundColor(new BaseColor(63, 81, 181));
        headerPrecio.setBackgroundColor(new BaseColor(63, 81, 181));
        headerNombre.setPadding(8);
        headerPrecio.setPadding(8);
        table.addCell(headerNombre);
        table.addCell(headerPrecio);

        // Filas de productos
        boolean colorAlterno = true;
        for (Producto producto : lista) {
            PdfPCell cellNombre = new PdfPCell(new Phrase(producto.getNombre(), fontProducto));
            PdfPCell cellPrecio = new PdfPCell(new Phrase(String.format("%.2f€", producto.getPrecio()), fontProducto));
            cellNombre.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellNombre.setPadding(5);
            cellPrecio.setPadding(5);

            // Alternar color de fondo
            if (colorAlterno) {
                cellNombre.setBackgroundColor(new BaseColor(224, 224, 224)); // Gris claro
                cellPrecio.setBackgroundColor(new BaseColor(224, 224, 224));
            }
            colorAlterno = !colorAlterno;

            table.addCell(cellNombre);
            table.addCell(cellPrecio);
        }
        
        document.add(table);
        document.add(new Paragraph(" "));

        // Subtotal
        double subtotal = lista.stream().mapToDouble(Producto::getPrecio).sum();
        Paragraph subtotalParagraph = new Paragraph("Subtotal: " + String.format("%.2f€", subtotal), fontSubtotal);
        subtotalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(subtotalParagraph);

        document.close();

        return byteArrayOutputStream.toByteArray();

    }

}
