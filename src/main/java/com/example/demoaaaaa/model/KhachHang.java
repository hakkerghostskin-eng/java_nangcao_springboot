package com.example.demoaaaaa.model;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Transient;

@Entity 
@Table (name = "khach_hang")
public class KhachHang {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer khach_hang_id;

    @Column (unique = true)
    private String ma_khach_hang;

    @NotBlank (message = "Ten khong duoc de trong")
    private String ho_ten;

    @NotBlank (message = "So dien thoai khong duoc de trong")
    @Pattern(regexp = "^\\+?0\\d{9,12}$", message = "So dien thoai khong hop le")
    /* Giải thích regex:
        ^: Bắt đầu chuỗi.
        \\+?: Cho phép có hoặc không có dấu +.
        0: Bắt buộc có số 0 ngay sau đó.
        \\d{9,12}: Theo sau là từ 9 đến 12 chữ số.
        $: Kết thúc chuỗi.
    */
    private String so_dien_thoai;

    @Email(message = "Dia chi email khong hop le")
    @NotBlank (message = "Email khong duoc de trong")
    @Column (unique = true)
    private String email;

    @NotBlank (message = "Mat khau khong duoc trong")
    @Size (min = 8, message = "Mat khau phai co it nhat 8 ky tu")
    private String mat_khau;

    
    private LocalDate ngay_sinh;

    private String gioi_tinh;

    private String dia_chi;

    private Integer diem_tich_luy;

    private Boolean trang_thai = true;

    @Transient 
    private String confirmPassword;

    // Getter + Setter khach_hang_id
    public Integer getKhachHangId() {
        return this.khach_hang_id;
    }

    public void setKhachHangId(Integer khach_hang_id) {
        this.khach_hang_id = khach_hang_id;
    }

    // Getter + Setter ma_khach_hang
    public String getMaKhachHang() {
        return this.ma_khach_hang;
    }

    public void setMaKhachHang(String ma_khach_hang) {
        this.ma_khach_hang = ma_khach_hang;
    }

    // Getter + Setter ho_ten
    public String getHoTen() {
        return this.ho_ten;
    }

    public void setHoTen(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    // Getter + Setter mat_khau
    public String getMatKhau() {
        return this.mat_khau;
    }

    public void setMatKhau(String mat_khau) {
        this.mat_khau = mat_khau;
    }

    // Getter + Setter confirmPassword
    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    // Getter + Setter so_dien_thoai
    public String getSoDienThoai() {
        return this.so_dien_thoai;
    }

    public void setSoDienThoai(String so_dien_thoai) {
        this.so_dien_thoai = so_dien_thoai;
    }

    // Getter + Setter email
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter + Setter ngay_sinh
    public LocalDate getNgaySinh() {
        return this.ngay_sinh;
    }

    public void setNgaySinh(LocalDate ngay_sinh) {
        this.ngay_sinh = ngay_sinh;
    }

    // Getter + Setter gioi_tinh
    public String getGioiTinh() {
        return this.gioi_tinh;
    }

    public void setGioiTinh(String gioi_tinh) {
        this.gioi_tinh = gioi_tinh;
    }


    // Getter + Setter dia_chi
    public String getDiaChi() {
        return this.dia_chi;
    }

    public void setDiaChi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    // Getter + Setter diem_tich_luy
    public Integer getDiemTichLuy() {
        return this.diem_tich_luy;
    }

    public void setDiemTichLuy(Integer diem_tich_luy) {
        this.diem_tich_luy = diem_tich_luy;
    }

    // Getter + Setter trang_thai
    public Boolean getTrangThai() {
        return this.trang_thai;
    }

    public void setTrangThai(Boolean trang_thai) {
        this.trang_thai = trang_thai;
    }

    
}
