package com.example.demoaaaaa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demoaaaaa.model.KhachHang;

public interface TaiKhoanRepository extends JpaRepository<KhachHang, Integer>{

    @Query("SELECT kh FROM KhachHang kh WHERE kh.email = :taiKhoan OR kh.so_dien_thoai = :taiKhoan")
    Optional<KhachHang> findByEmailOrSoDienThoai(@Param("taiKhoan") String taiKhoan);
    boolean existsBySoDienThoai(String soDienThoai);
    
    @Query("SELECT CASE WHEN COUNT(kh) > 0 THEN TRUE ELSE FALSE END " +
       "FROM KhachHang kh WHERE kh.email = :email")
    boolean kiemTraEmailTonTai(@Param("email") String email);
}
