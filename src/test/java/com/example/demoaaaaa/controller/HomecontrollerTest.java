package com.example.demoaaaaa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ExtendedModelMap;

import com.example.demoaaaaa.model.SanPham;
import com.example.demoaaaaa.service.CartService;
import com.example.demoaaaaa.service.DanhMucService;
import com.example.demoaaaaa.service.SanPhamService;
import com.example.demoaaaaa.service.SizeService;
import com.example.demoaaaaa.service.ToppingService;

import jakarta.servlet.http.HttpServletRequest;

class HomecontrollerTest {
    private static final int PAGE_SIZE = 16;

    private final DanhMucService danhMucService = mock(DanhMucService.class);
    private final CartService cartService = mock(CartService.class);
    private final SanPhamService sanPhamService = mock(SanPhamService.class);
    private final ToppingService toppingService = mock(ToppingService.class);
    private final SizeService sizeService = mock(SizeService.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    private Homecontroller homecontroller;
    private List<SanPham> products;

    @BeforeEach
    void setUp() {
        homecontroller = new Homecontroller(danhMucService, cartService, sanPhamService, toppingService, sizeService);
        products = new ArrayList<>();
        for (int id = 1; id <= 35; id++) {
            SanPham product = new SanPham();
            product.setIdSanPham(id);
            products.add(product);
        }
        when(sanPhamService.getSanPhamPage(any(Pageable.class))).thenAnswer(invocation -> pageFor(
                invocation.getArgument(0)));
    }

    @Test
    void returnsNonOverlappingPagesAndClampsInvalidPageIndexes() {
        Page<?> firstPage = getPage(0);
        Page<?> middlePage = getPage(1);
        Page<?> lastPage = getPage(2);

        assertEquals(16, firstPage.getContent().size());
        assertEquals(16, middlePage.getContent().size());
        assertEquals(3, lastPage.getContent().size());
        assertTrue(firstPage.getContent().stream().noneMatch(middlePage.getContent()::contains));
        assertTrue(firstPage.getContent().stream().noneMatch(lastPage.getContent()::contains));
        assertTrue(middlePage.getContent().stream().noneMatch(lastPage.getContent()::contains));

        ExtendedModelMap negativePageModel = getModel(-1);
        assertEquals(0, negativePageModel.getAttribute("currentPage"));
        ExtendedModelMap excessivePageModel = getModel(9999);
        assertEquals(2, excessivePageModel.getAttribute("currentPage"));
        assertEquals(3, excessivePageModel.getAttribute("totalPages"));
        assertEquals(35L, excessivePageModel.getAttribute("totalProducts"));
    }

    @Test
    void keepsHomeDataAndHandlesAnEmptyProductList() {
        when(sanPhamService.getSanPhamPage(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), org.springframework.data.domain.PageRequest.of(0, PAGE_SIZE), 0));
        ExtendedModelMap model = getModel(0);

        assertTrue(((List<?>) model.getAttribute("sanPhams")).isEmpty());
        assertEquals(0, model.getAttribute("totalPages"));
        assertEquals(0L, model.getAttribute("totalProducts"));
        assertTrue(model.containsAttribute("danhMucs"));
        assertTrue(model.containsAttribute("toppings"));
        assertTrue(model.containsAttribute("cartCount"));
    }

    @Test
    void keepsProductDetailAvailable() {
        SanPham product = products.get(0);
        when(sanPhamService.getSanPham(1)).thenReturn(Optional.of(product));
        when(toppingService.getAllTopping()).thenReturn(List.of());
        when(sizeService.getAllSize()).thenReturn(List.of());
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("product-detail", homecontroller.chiTietSanPham(1, request, model));
        assertSame(product, model.getAttribute("sanPham"));
    }

    private Page<?> getPage(int requestedPage) {
        ExtendedModelMap model = getModel(requestedPage);
        Object page = model.getAttribute("sanPhamPage");
        if (page instanceof Page<?> result) {
            return result;
        }
        throw new AssertionError("Expected paged product data in the model");
    }

    private ExtendedModelMap getModel(int requestedPage) {
        ExtendedModelMap model = new ExtendedModelMap();
        assertEquals("home", homecontroller.home(request, requestedPage, model));
        return model;
    }

    private Page<SanPham> pageFor(Pageable pageable) {
        assertEquals(PAGE_SIZE, pageable.getPageSize());
        Sort.Order idOrder = pageable.getSort().getOrderFor("idSanPham");
        assertFalse(idOrder.isDescending());
        int start = (int) Math.min(pageable.getOffset(), products.size());
        int end = Math.min(start + pageable.getPageSize(), products.size());
        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }
}
