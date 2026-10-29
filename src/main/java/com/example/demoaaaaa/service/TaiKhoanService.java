package com.example.demoaaaaa.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.KhachHang;
import com.example.demoaaaaa.repository.TaiKhoanRepository;

@Service 
public class TaiKhoanService {
    // ma hoa mat khau, phai them vao .pom
    // @Autowired 
    // private PassWordEncode passWordEncode;
    private final TaiKhoanRepository TaiKhoanRepository;

    public TaiKhoanService(TaiKhoanRepository TaiKhoanRepository) {
        this.TaiKhoanRepository = TaiKhoanRepository;
    }
    
    // all method
    public KhachHang dangKyKH(KhachHang kh) {
        // Tạo mã khách hàng duy nhất, tối đa 30 ký tự
        kh.setMaKhachHang("KH" + System.currentTimeMillis());
        // Set ngày sinh mặc định nếu không được cung cấp
        kh.setNgaySinh(LocalDate.of(2002, 1, 1));
        // Xử lý giới tính không được chọn
        if (kh.getGioiTinh() == null || kh.getGioiTinh().isBlank()) {
            kh.setGioiTinh("KHAC");
        }

        // viet ma hoa mat khau
        // kh.setMatKhau(passWordEncode.encode(kh.getMatKhau()));
        return TaiKhoanRepository.save(kh);
    }
}
