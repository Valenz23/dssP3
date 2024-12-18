package com.example.demo.controller;

import com.example.demo.model.CarritoElemento;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CarritoService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final Map<String, List<CarritoElemento>> userCarts = new HashMap<>();
    private final ProductRepository productRepository;
    private final CarritoService carritoService; 

    public CarritoController(ProductRepository productRepository) {
        this.productRepository = productRepository;
		this.carritoService = new CarritoService();
    }

    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<CarritoElemento> carrito = userCarts.getOrDefault(username, new ArrayList<>());
        double totalCarrito = carrito.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", totalCarrito); 
        System.out.println("Carrito: " + carrito);
        System.out.println("Total: " + totalCarrito);
        
        return "carrito";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam int quantity, Authentication authentication) {
        String username = authentication.getName();
        List<CarritoElemento> cart = userCarts.computeIfAbsent(username, k -> new ArrayList<>());

        productRepository.findById(productId).ifPresent(product -> {
            boolean exists = false;
            for (CarritoElemento item : cart) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                cart.add(new CarritoElemento(product, quantity));
            }
        });
        return "redirect:/products";
    }

    
    @PostMapping("/update/{productId}")
    public String updateCartItem(
            @PathVariable Long productId,
            @RequestParam int quantity,
            Authentication authentication) 
    {
        if (authentication == null) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        String username = authentication.getName();

        List<CarritoElemento> carrito = userCarts.computeIfAbsent(username, k -> new ArrayList<>());

        boolean found = false;
        for (CarritoElemento item : carrito) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Producto no encontrado en el carrito");
        }

        return "redirect:/carrito";
    }
    
    @PostMapping("/remove/{productId}")
    public String removeCartItem(@PathVariable Long productId, Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        String username = authentication.getName();

        List<CarritoElemento> carrito = userCarts.computeIfAbsent(username, k -> new ArrayList<>());
        
        carrito.removeIf(item -> item.getProduct().getId().equals(productId));

        return "redirect:/carrito";
    }
    
    @GetMapping("/finalizar")
    public void finalizarCompra(Authentication authentication, HttpServletResponse response) throws IOException, DocumentException {
    	String username = authentication.getName();
        List<CarritoElemento> carrito = userCarts.getOrDefault(username, new ArrayList<>());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=carrito_compra.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Ticket de Compra", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell(new Phrase("Producto", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(new Phrase("Precio", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(new Phrase("Cantidad", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

        double totalCarrito = 0.0;

        for (CarritoElemento item : carrito) {
            table.addCell(item.getProduct().getName());
            table.addCell(String.format("%.2f €", item.getProduct().getPrice()));
            table.addCell(String.valueOf(item.getQuantity()));
            double total = item.getProduct().getPrice() * item.getQuantity();
            table.addCell(String.format("%.2f €", total));
            totalCarrito += total;
        }

        table.addCell("");
        table.addCell("");
        table.addCell("Total:");
        table.addCell(String.format("%.2f €", totalCarrito));

        document.add(table);
        document.close();

        userCarts.put(username, new ArrayList<>()); // Vaciar el carrito
    }
}
