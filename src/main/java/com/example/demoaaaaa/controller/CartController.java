package com.example.demoaaaaa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demoaaaaa.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/gio-hang")
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("items", cartService.getCartItems(session));
        model.addAttribute("cartKeys", cartService.getCartKeys(session));
        model.addAttribute("total", cartService.getTotal(session));
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "giohang";
    }

    @PostMapping("/gio-hang/them")
    public String addItem(@RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer sizeId,
            @RequestParam(required = false) List<Integer> toppingIds,
            @RequestParam(required = false) Integer quantity,
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            cartService.addItem(session, productId, sizeId, toppingIds, quantity);
            redirectAttributes.addFlashAttribute("thongBao", "Đã thêm sản phẩm vào giỏ hàng.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("loi", exception.getMessage());
        }
        return "redirect:/gio-hang";
    }

    @PostMapping("/gio-hang/cap-nhat")
    public String updateQuantity(@RequestParam(required = false) String key,
            @RequestParam(required = false) Integer quantity, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(session, key, quantity);
            redirectAttributes.addFlashAttribute("thongBao",
                    quantity == 0 ? "Đã xóa sản phẩm khỏi giỏ hàng." : "Đã cập nhật số lượng.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("loi", exception.getMessage());
        }
        return "redirect:/gio-hang";
    }

    @PostMapping("/gio-hang/xoa")
    public String removeItem(@RequestParam(required = false) String key, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            cartService.removeItem(session, key);
            redirectAttributes.addFlashAttribute("thongBao", "Đã xóa sản phẩm khỏi giỏ hàng.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("loi", exception.getMessage());
        }
        return "redirect:/gio-hang";
    }

    @PostMapping("/gio-hang/xoa-tat-ca")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute("thongBao", "Đã xóa toàn bộ giỏ hàng.");
        return "redirect:/gio-hang";
    }
}
