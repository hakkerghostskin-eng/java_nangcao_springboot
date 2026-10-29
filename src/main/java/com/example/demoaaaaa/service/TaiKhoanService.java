package com.example.demoaaaaa.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.KhachHang;
import com.example.demoaaaaa.repository.TaiKhoanRepository;

@Service 
public class TaiKhoanService {
    private final PasswordEncoder passWordEncode = new BCryptPasswordEncoder();
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

        if (TaiKhoanRepository.kiemTraEmailTonTai(kh.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        kh.setMatKhau(passWordEncode.encode(kh.getMatKhau()));
        return TaiKhoanRepository.save(kh);
    }

    public Optional<KhachHang> dangNhap(String taiKhoan, String matKhau) {
        if (taiKhoan == null || taiKhoan.isBlank() || matKhau == null || matKhau.isBlank()) {
            return Optional.empty();
        }

        return TaiKhoanRepository.findByEmailOrSoDienThoai(taiKhoan.trim()).filter(kh -> Boolean.TRUE.equals(kh.getTrangThai())).filter(kh -> kh.getMatKhau() != null && passWordEncode.matches(matKhau, kh.getMatKhau()));
    }
}
