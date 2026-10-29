package com.example.demoaaaaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "kich_thuoc")
public class Size {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "size_id")
    private Integer idSize;

    @Column (name = "ma_size")
    private String maSize;

    @Column (name = "ten_size")
    private String tenSize;

    @Column (name = "phu_thu")
    private double phuThu;

    @Column (name = "thu_tu")
    private int thuTu;

    @Column (name = "trang_thai")
    private Boolean trangThai;

    // Constructor
    public Size() {}

    // Getter + Setter idSize
    public Integer getIdSize() {
        return idSize;
    }

    public void setIdSize(Integer idSize) {
        this.idSize = idSize;
    }
    // Getter + Setter maSize
    public String getMaSize() {
        return maSize;
    }

    public void setMaSize(String maSize) {
        this.maSize = maSize;
    }
    // Getter + Setter tenSize
    public String getTenSize() {
        return tenSize;
    }

    public void setTenSize(String tenSize) {
        this.tenSize = tenSize;
    }
    // Getter + Setter phuThuSize
    public double getPhuThuSize() {
        return phuThu;
    }

    public void setPhuThuSize(double phuThuSize) {
        this.phuThu = phuThuSize;
    }
    // Getter + Setter thuTu
    public int getThuTu() {
        return thuTu;
    }

    public void setThuTu(int thuTu) {
        this.thuTu = thuTu;
    }
    // Getter + Setter trangThai
    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
}
