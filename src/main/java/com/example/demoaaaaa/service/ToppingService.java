package com.example.demoaaaaa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.Topping;
import com.example.demoaaaaa.repository.ToppingRepository;

@Service 
public class ToppingService {
    private final ToppingRepository toppingRepository;

    // Constructor
    public ToppingService(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    // All methods for ToppingService
    public List<Topping> getAllTopping() {
        return toppingRepository.findAll();
    }
}
