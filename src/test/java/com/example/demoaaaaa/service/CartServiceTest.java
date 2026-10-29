package com.example.demoaaaaa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demoaaaaa.model.CartItem;
import com.example.demoaaaaa.model.SanPham;
import com.example.demoaaaaa.model.Size;
import com.example.demoaaaaa.model.Topping;
import com.example.demoaaaaa.repository.SizeRepository;
import com.example.demoaaaaa.repository.ToppingRepository;

import jakarta.servlet.http.HttpSession;

class CartServiceTest {
    private SanPhamService sanPhamService;
    private SizeRepository sizeRepository;
    private ToppingRepository toppingRepository;
    private CartService cartService;
    private SanPham product;
    private Size medium;
    private Size large;
    private Topping pearl;

    @BeforeEach
    void setUp() {
        sanPhamService = mock(SanPhamService.class);
        sizeRepository = mock(SizeRepository.class);
        toppingRepository = mock(ToppingRepository.class);
        cartService = new CartService(sanPhamService, sizeRepository, toppingRepository);

        product = new SanPham();
        product.setIdSanPham(1);
        product.setTenSanPham("Trà sữa");
        product.setHinhAnh("tra-sua.jpg");
        product.setGiaCoBan(100.25);
        product.setTrangThai(true);

        medium = new Size();
        medium.setIdSize(1);
        medium.setTenSize("M");
        medium.setPhuThuSize(20.25);
        medium.setTrangThai(true);

        large = new Size();
        large.setIdSize(2);
        large.setTenSize("L");
        large.setPhuThuSize(30.25);
        large.setTrangThai(true);

        pearl = new Topping();
        pearl.setIdTopping(10);
        pearl.setTenTopping("Trân châu");
        pearl.setGiaBan(5.5);
        pearl.setTrangThai(false);

        when(sanPhamService.getSanPham(1)).thenReturn(Optional.of(product));
        when(sizeRepository.findById(1)).thenReturn(Optional.of(medium));
        when(sizeRepository.findById(2)).thenReturn(Optional.of(large));
        when(toppingRepository.findById(10)).thenReturn(Optional.of(pearl));
        when(toppingRepository.findAll()).thenReturn(List.of(pearl));
    }

    @Test
    void startsWithEmptySessionCart() {
        HttpSession session = newSession();

        assertEquals(0, cartService.getCartItems(session).size());
        assertInstanceOf(java.util.LinkedHashMap.class, session.getAttribute(CartService.CART_SESSION_KEY));
        assertEquals(BigDecimal.ZERO, cartService.getTotal(session));
    }

    @Test
    void mergesSameConfigurationAndCalculatesMoneyWithBigDecimal() {
        HttpSession session = newSession();
        cartService.addItem(session, 1, 1, List.of(10), 2);
        cartService.addItem(session, 1, 1, List.of(10, 10), 3);

        List<CartItem> items = cartService.getCartItems(session);
        assertEquals(1, items.size());
        assertEquals(5, items.get(0).getQuantity());
        assertEquals(new BigDecimal("126.00"), items.get(0).getUnitPrice());
        assertEquals(new BigDecimal("630.00"), items.get(0).getLineTotal());
        assertEquals(new BigDecimal("630.00"), cartService.getTotal(session));
    }

    @Test
    void differentSizesAndToppingsCreateDifferentLines() {
        Topping pudding = new Topping();
        pudding.setIdTopping(11);
        pudding.setTenTopping("Pudding");
        pudding.setGiaBan(8.0);
        pudding.setTrangThai(false);
        when(toppingRepository.findById(11)).thenReturn(Optional.of(pudding));
        when(toppingRepository.findAll()).thenReturn(List.of(pearl, pudding));

        HttpSession session = newSession();
        cartService.addItem(session, 1, 1, List.of(10), 1);
        cartService.addItem(session, 1, 2, List.of(10), 1);
        cartService.addItem(session, 1, 1, List.of(11), 1);

        assertEquals(3, cartService.getCartItems(session).size());
    }

    @Test
    void rejectsNonPositiveQuantityAndMissingProducts() {
        HttpSession session = newSession();

        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(session, 1, 1, List.of(), 0));
        when(sanPhamService.getSanPham(99)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(session, 99, 1, List.of(), 1));
    }

    @Test
    void updatesQuantityAndRejectsUnavailableSizesAndRequiredToppings() {
        HttpSession session = newSession();
        cartService.addItem(session, 1, 1, List.of(), 1);
        cartService.updateQuantity(session, cartService.getCartKeys(session).get(0), 4);
        assertEquals(4, cartService.getCartCount(session));

        assertThrows(IllegalArgumentException.class,
                () -> cartService.updateQuantity(session, cartService.getCartKeys(session).get(0), -1));
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(session, 1, 99, List.of(), 1));

        pearl.setTrangThai(true);
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(session, 1, 1, List.of(), 1));
    }

    @Test
    void keepsSessionsIndependentAndClearsOnlyCartAttribute() {
        HttpSession firstSession = newSession();
        HttpSession secondSession = newSession();
        cartService.addItem(firstSession, 1, 1, List.of(), 1);
        cartService.addItem(secondSession, 1, 2, List.of(), 2);
        firstSession.setAttribute("khachHang", "kept");

        assertEquals(1, cartService.getCartCount(firstSession));
        assertEquals(2, cartService.getCartCount(secondSession));
        cartService.clearCart(firstSession);

        assertEquals("kept", firstSession.getAttribute("khachHang"));
        assertEquals(0, cartService.getCartCount(firstSession));
        assertEquals(2, cartService.getCartCount(secondSession));
    }

    @Test
    void removesOnlyRequestedLineAndRejectsUnknownKey() {
        HttpSession session = newSession();
        cartService.addItem(session, 1, 1, List.of(), 1);
        cartService.addItem(session, 1, 2, List.of(), 1);
        String key = cartService.getCartKeys(session).get(0);

        cartService.removeItem(session, key);

        assertEquals(1, cartService.getCartItems(session).size());
        assertThrows(IllegalArgumentException.class, () -> cartService.removeItem(session, "missing"));
    }

    @Test
    void updatingQuantityToZeroRemovesOnlyThatLine() {
        HttpSession session = newSession();
        cartService.addItem(session, 1, 1, List.of(), 2);
        cartService.addItem(session, 1, 2, List.of(), 1);
        String key = cartService.getCartKeys(session).get(0);

        cartService.updateQuantity(session, key, 0);

        assertEquals(1, cartService.getCartItems(session).size());
        assertEquals(1, cartService.getCartCount(session));
    }

    private HttpSession newSession() {
        HttpSession session = mock(HttpSession.class);
        Map<String, Object> attributes = new HashMap<>();
        when(session.getAttribute(org.mockito.ArgumentMatchers.anyString()))
                .thenAnswer(invocation -> attributes.get(invocation.getArgument(0)));
        doAnswer(invocation -> {
            attributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(session).setAttribute(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any());
        doAnswer(invocation -> {
            attributes.remove(invocation.getArgument(0));
            return null;
        }).when(session).removeAttribute(org.mockito.ArgumentMatchers.anyString());
        return session;
    }
}
