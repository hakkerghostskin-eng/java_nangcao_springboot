package com.example.demoaaaaa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.DanhMuc;
import com.example.demoaaaaa.repository.DanhMucRepository;

@Service 
public class DanhMucService {
    private final DanhMucRepository danhMucRepository;
    public DanhMucService(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }
}
