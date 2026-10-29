package com.example.demoaaaaa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.Size;
import com.example.demoaaaaa.repository.SizeRepository;

@Service 
public class SizeService {
    private final SizeRepository sizeRepository;


    // Constructor
    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }
    // all methods for SizeService
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

}
