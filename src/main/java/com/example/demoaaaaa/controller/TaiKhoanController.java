package com.example.demoaaaaa.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demoaaaaa.model.KhachHang;
import com.example.demoaaaaa.service.TaiKhoanService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller 
public class TaiKhoanController {
    public final TaiKhoanService tkService;

    public TaiKhoanController(TaiKhoanService tkService) {
        this.tkService = tkService;
    }
    

    @GetMapping("/dang-ky")
    public String hienthiDangKy(Model model) {
        model.addAttribute("khachhang", new KhachHang());
        return "dang-ky";
    
    }
    @GetMapping("/dang-nhap")
    public String hienthiDangNhap(Model model) {
        model.addAttribute("khachhang", new KhachHang());
        return "dang-nhap";
    }

    @PostMapping("/dang-nhap")
    public String dangNhap( @RequestParam(name = "taiKhoan", required = false) String taiKhoan, @RequestParam(name = "matKhau", required = false) String matKhau, HttpSession session, Model model) {
        KhachHang khachHang = tkService.dangNhap(taiKhoan, matKhau).orElse(null);
        if (khachHang == null) {
            model.addAttribute("loiDangNhap", "Email/số điện thoại hoặc mật khẩu không chính xác.");
            return "dang-nhap";
        }

        session.setAttribute("khachHang", khachHang);
        return "redirect:/";
    }

    @GetMapping("/dang-xuat")
    public String dangXuat(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/quen-mat-khau")
    public String hienThiQuenMatKhau() {
        return "quen-mat-khau";
    }

    @PostMapping("/dang-ky")
    public String dangKyKhachHang(@Valid @ModelAttribute("khachhang") KhachHang khachhang, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        // Kiểm tra xác nhận mật khẩu
        if (khachhang.getMatKhau() == null || khachhang.getConfirmPassword() == null || khachhang.getConfirmPassword().isBlank()) {

            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Vui long nhap xac nhan mat khau");

        } else if (!khachhang.getMatKhau().equals(khachhang.getConfirmPassword())) {

            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Mat khau xac nhan khong khop");
        }

        // Nếu dữ liệu không hợp lệ, hiển thị lại form
        if (bindingResult.hasErrors()) {
            // In toàn bộ lỗi
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Loi: " + error);
            });


            return "dang-ky";
        }

        try {
            tkService.dangKyKH(khachhang);

            redirectAttributes.addFlashAttribute("thongBao", "Dang ky tai khoan thanh cong!");

            return "redirect:/dang-nhap";

        } catch (IllegalArgumentException e) {
            model.addAttribute("loiDangKy", e.getMessage());
            return "dang-ky";
        }
    }

}
