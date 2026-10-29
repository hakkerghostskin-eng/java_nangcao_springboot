package com.example.demoaaaaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "san_pham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "san_pham_id")
    private Integer idSanPham;

    @Column(name = "ma_san_pham")
    private String maSanPham;

    @Column(name = "ten_san_pham")
    private String tenSanPham;
    
    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "hinh_anh")
    private String hinhAnh;
    
    @Column(name = "gia_co_ban")
    private double giaCoBan;
    
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "noi_bat")
    private Boolean noiBat;

    @Column(name = "ban_chay")
    private Boolean banChay;

    @ManyToOne
    @JoinColumn(name = "danh_muc_id")
    private DanhMuc danhMuc;

    // Constructor
    public SanPham() {}


    // Getter + Setter idSanPham
    public Integer getIdSanPham() {
        return this.idSanPham;
    }

    public void setIdSanPham(Integer idSanPham) {
        this.idSanPham = idSanPham;
    }

    // Getter + Setter maSanPham
    public String getMaSanPham() {
        return this.maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    // Getter + Setter tenSanPham
    public String getTenSanPham() {
        return this.tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }


    // Getter + Setter moTa
    public String getMoTa() {
        return this.moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }


    // Getter + Setter hinhAnh
    public String getHinhAnh() {
        return this.hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    // Getter + Setter giaCoBan
    public double getGiaCoBan() {
        return this.giaCoBan;
    }
    public void setGiaCoBan(double giaCoBan) {
        this.giaCoBan = giaCoBan;
    }

    // Getter + Setter trangThai
    public Boolean getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }


    // Getter + Setter noiBat
    public Boolean getNoiBat() {
        return this.noiBat;
    }

    public void setNoiBat(Boolean noiBat) {
        this.noiBat = noiBat;
    }


    // Getter + Setter banChay
    public Boolean getBanChay() {
        return this.banChay;
    }

    public void setBanChay(Boolean banChay) {
        this.banChay = banChay;
    }


    // Getter + Setter danhMuc
    public DanhMuc getDanhMuc() {
        return this.danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }


    
}
