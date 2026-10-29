package com.example.demoaaaaa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.SanPham;
import com.example.demoaaaaa.repository.SanPhamRepository;

@Service 
public class SanPhamService {
    private final SanPhamRepository sanPhamRepository;

    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    public Page<SanPham> getSanPhamPage(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    public Optional<SanPham> getSanPham(Integer sanphamId)  {
        return sanPhamRepository.findById(sanphamId);
    }
}
