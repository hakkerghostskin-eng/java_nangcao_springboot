package com.example.demoaaaaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;


@Entity
@Table (name = "topping")
public class Topping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "topping_id")
    private Integer idTopping;

    @Column (name = "ten_topping")
    private String tenTopping;

    @Column (name = "ma_topping")
    private String maTopping;

    @Column (name = "mo_ta")
    private String motaTopping;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column (name = "gia_ban")
    private double giaBan;

    @Column (name = "noi_bat")
    private Integer noiBat; // 1: Nổi bật, 0: Không nổi bật
    
    @Column (name = "trang_thai")
    private Boolean trangThai; // 1: Bắt buộc, 0: Tùy chọn

    // constructor
    public Topping() {
    }
    
    // Getter + Setter idTopping
    public Integer getIdTopping() {
        return idTopping;
    }

    public void setIdTopping(Integer idTopping) {
        this.idTopping = idTopping;
    }
    // Getter + Setter tenTopping
    public String getTenTopping() {
        return tenTopping;
    }

    public void setTenTopping(String tenTopping) {
        this.tenTopping = tenTopping;
    }

    // Getter + Setter maTopping
    public String getMaTopping() {
        return maTopping;
    }
    public void setMaTopping(String maTopping) {
        this.maTopping = maTopping;
    }
    // Getter + Setter motaTopping
    public String getMotaTopping() {
        return motaTopping;
    }
    public void setMotaTopping(String motaTopping) {
        this.motaTopping = motaTopping;
    }
    // Getter + Setter hinhAnh
    public String getHinhAnh() {
        return hinhAnh;
    }
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    // Getter + Setter giaBan
    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    // Getter + Setter noiBat
    public Integer getNoiBat() {
        return noiBat;
    }

    public void setNoiBat(Integer noiBat) {
        this.noiBat = noiBat;
    }

    // Getter + Setter trangThai
    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    
}
