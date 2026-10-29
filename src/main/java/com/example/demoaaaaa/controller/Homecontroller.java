package com.example.demoaaaaa.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.demoaaaaa.model.SanPham;
import com.example.demoaaaaa.model.Size;
import com.example.demoaaaaa.model.Topping;
import com.example.demoaaaaa.service.DanhMucService;
import com.example.demoaaaaa.service.CartService;
import com.example.demoaaaaa.service.SanPhamService;
import com.example.demoaaaaa.service.SizeService;
import com.example.demoaaaaa.service.ToppingService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

@Controller 
public class Homecontroller {
    private static final int PAGE_SIZE = 16;

    private final DanhMucService danhMucService;
    private final CartService cartService;
    private final SanPhamService sanPhamService;
    private final ToppingService toppingService;
    private final SizeService sizeService;
    
    // Constructor
    public Homecontroller(DanhMucService danhMucService, CartService cartService, SanPhamService sanPhamService,
            ToppingService toppingService, SizeService sizeService) {
        this.danhMucService = danhMucService;
        this.cartService = cartService;
        this.sanPhamService = sanPhamService;
        this.toppingService = toppingService;
        this.sizeService = sizeService;
    }


    @GetMapping("/")
    public String home(HttpServletRequest request, @RequestParam(defaultValue = "0") int page, Model model) {
        int requestedPage = Math.max(page, 0);
        Page<SanPham> sanPhamPage = sanPhamService.getSanPhamPage(createPageRequest(requestedPage));
        if (sanPhamPage.getTotalPages() == 0) {
            requestedPage = 0;
        } else if (requestedPage >= sanPhamPage.getTotalPages()) {
            requestedPage = sanPhamPage.getTotalPages() - 1;
            sanPhamPage = sanPhamService.getSanPhamPage(createPageRequest(requestedPage));
        }

        model.addAttribute("danhMucs", danhMucService.getAllDanhMuc());
        model.addAttribute("sanPhams", sanPhamPage.getContent());
        model.addAttribute("sanPhamPage", sanPhamPage);
        model.addAttribute("currentPage", requestedPage);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("totalProducts", sanPhamPage.getTotalElements());
        model.addAttribute("toppings", toppingService.getAllTopping());
        model.addAttribute("cartCount", cartService.getCartCount(request.getSession(false)));

        return "home";
    }

    private PageRequest createPageRequest(int page) {
        return PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "idSanPham"));
    }

    @GetMapping("/san-pham/{id}")
    public String chiTietSanPham(@PathVariable Integer id, HttpServletRequest request, Model model) {
        //c1
        SanPham sanPham = sanPhamService.getSanPham(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("sanPham", sanPham);
        //c2 
        // Optional<SanPham> sanPham = sanPhamService.getSanPham(id);
        // if (sanPham.isPresent()) {
        //     SanPham product = sanPham.get();
        //     model.addAttribute("sanPham", product);
        //     // return ResponseEntity.ok(product);
            
        // }
        // return ResponseEntity.notFound().build();
        List<Topping> toppings = toppingService.getAllTopping();
        model.addAttribute("toppings", toppings);
        List<Size> sizes = sizeService.getAllSize().stream()
                .filter(size -> Boolean.TRUE.equals(size.getTrangThai()))
                .toList();
        model.addAttribute("sizes", sizes);
        model.addAttribute("cartCount", cartService.getCartCount(request.getSession(false)));
        return "product-detail";
        
    }
}
