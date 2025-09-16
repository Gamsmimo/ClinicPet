package com.clinicpet.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.clinicpet.demo.model.Carrito;
import com.clinicpet.demo.model.CarritoProducto;

import com.clinicpet.demo.service.ICarritoService;
import com.clinicpet.demo.service.ICarritoProductoService;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ICarritoService carritoService;

    @Autowired
    private ICarritoProductoService carritoProductoService;

    // Listar todos los carritos
    @GetMapping("/listar")
    public String listarCarritos(Model model) {
        List<Carrito> carritos = carritoService.listarCarritos(null);
        model.addAttribute("carritos", carritos);
        return "carrito/listar";
    }

    // Ver detalle del carrito (con productos)
    @GetMapping("/{id}")
    public String verCarrito(@PathVariable("id") Integer id, Model model) {
        Optional<Carrito> carrito = carritoService.buscarPorId(id);
        if (carrito.isPresent()) {
            model.addAttribute("carrito", carrito.get());

            // Para el formulario de agregar producto
            model.addAttribute("carritoProducto", new CarritoProducto());
            return "carrito/detalle";
        } else {
            return "redirect:/carrito/listar";
        }
    }

    // Crear carrito
    @GetMapping("/nuevo")
    public String nuevoCarrito(Model model) {
        model.addAttribute("carrito", new Carrito());
        return "carrito/form";
    }

    @PostMapping("/guardar")
    public String guardarCarrito(@ModelAttribute Carrito carrito) {
        carritoService.guardarCarrito(carrito);
        return "redirect:/carrito/listar";
    }

    // Eliminar carrito
    @GetMapping("/eliminar/{id}")
    public String eliminarCarrito(@PathVariable("id") Integer id) {
        carritoService.eliminarCarrito(id);
        return "redirect:/carrito/listar";
    }

    // -------------------------------
    // 🔹 PRODUCTOS DEL CARRITO
    // -------------------------------

    // Agregar producto al carrito
    @PostMapping("/{carritoId}/agregarProducto")
    public String agregarProducto(
            @PathVariable("carritoId") Integer carritoId,
            @ModelAttribute CarritoProducto carritoProducto) {

        // asociar el carrito al producto antes de guardar
        Carrito carrito = carritoService.buscarPorId(carritoId).orElse(null);
        if (carrito != null) {
            carritoProducto.setCarrito(carrito);
            carritoProductoService.guardarCarritoPrroducto(carritoProducto);
        }
        return "redirect:/carrito/" + carritoId;
    }

    // Eliminar producto del carrito
    @GetMapping("/{carritoId}/eliminarProducto/{productoId}")
    public String eliminarProducto(
            @PathVariable("carritoId") Integer carritoId,
            @PathVariable("productoId") Integer productoId) {

        carritoProductoService.eliminarCarritoProducto(productoId);
        return "redirect:/carrito/" + carritoId;
    }
}
