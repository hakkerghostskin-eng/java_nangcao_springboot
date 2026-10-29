package com.example.demoaaaaa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.example.demoaaaaa.model.SanPham;
import com.example.demoaaaaa.model.Size;
import com.example.demoaaaaa.model.Topping;
import com.example.demoaaaaa.service.DanhMucService;
import com.example.demoaaaaa.service.SanPhamService;
import com.example.demoaaaaa.service.SizeService;
import com.example.demoaaaaa.service.ToppingService;

import org.springframework.ui.Model;

@Controller 
public class Homecontroller {
    private final DanhMucService danhMucService;
    private final SanPhamService sanPhamService;
    private final ToppingService toppingService;
    private final SizeService sizeService;
    
    // Constructor
    public Homecontroller(DanhMucService danhMucService, SanPhamService sanPhamService, ToppingService toppingService, SizeService sizeService) {
        this.danhMucService = danhMucService;
        this.sanPhamService = sanPhamService;
        this.toppingService = toppingService;
        this.sizeService = sizeService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("danhMucs", danhMucService.getAllDanhMuc());
        model.addAttribute("sanPhams", sanPhamService.getAllSanPham());
        model.addAttribute("toppings", toppingService.getAllTopping());
        return "home";
    }

    @GetMapping("/san-pham/{id}")
    public String chiTietSanPham(@PathVariable Integer id, Model model) {
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
        List<Topping> toppings = toppingService.getAllTopping(); // Example: get the first topping
        model.addAttribute("toppings", toppings);
        List<Size> sizes = sizeService.getAllSize(); // Example: get the first size
        model.addAttribute("sizes", sizes);
        return "product-detail";
        
    }
}

