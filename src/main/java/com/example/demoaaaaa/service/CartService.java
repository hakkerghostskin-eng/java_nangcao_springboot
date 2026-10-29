package com.example.demoaaaaa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demoaaaaa.model.CartItem;
import com.example.demoaaaaa.model.CartTopping;
import com.example.demoaaaaa.model.SanPham;
import com.example.demoaaaaa.model.Size;
import com.example.demoaaaaa.model.Topping;
import com.example.demoaaaaa.repository.SizeRepository;
import com.example.demoaaaaa.repository.ToppingRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
    public static final String CART_SESSION_KEY = "CART";

    private final SanPhamService sanPhamService;
    private final SizeRepository sizeRepository;
    private final ToppingRepository toppingRepository;

    public CartService(SanPhamService sanPhamService, SizeRepository sizeRepository,
            ToppingRepository toppingRepository) {
        this.sanPhamService = sanPhamService;
        this.sizeRepository = sizeRepository;
        this.toppingRepository = toppingRepository;
    }

    public Map<String, CartItem> getCart(HttpSession session) {
        Object currentCart = session.getAttribute(CART_SESSION_KEY);
        if (currentCart == null) {
            Map<String, CartItem> cart = new LinkedHashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
            return cart;
        }
        return validateCart(currentCart);
    }

    public List<CartItem> getCartItems(HttpSession session) {
        return new ArrayList<>(getCart(session).values());
    }

    public List<String> getCartKeys(HttpSession session) {
        return new ArrayList<>(getCart(session).keySet());
    }

    public int getCartCount(HttpSession session) {
        if (session == null) {
            return 0;
        }
        Object currentCart = session.getAttribute(CART_SESSION_KEY);
        if (currentCart == null) {
            return 0;
        }
        return validateCart(currentCart).values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public BigDecimal getTotal(HttpSession session) {
        return getCart(session).values().stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(HttpSession session, Integer productId, Integer sizeId, List<Integer> toppingIds,
            Integer quantity) {
        if (productId == null || productId <= 0 || sizeId == null || sizeId <= 0) {
            throw new IllegalArgumentException("Vui lòng chọn sản phẩm và kích cỡ hợp lệ.");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }

        SanPham product = sanPhamService.getSanPham(productId)
                .filter(item -> !Boolean.FALSE.equals(item.getTrangThai()))
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại hoặc hiện không thể mua."));
        Size size = sizeRepository.findById(sizeId)
                .filter(item -> Boolean.TRUE.equals(item.getTrangThai()))
                .orElseThrow(() -> new IllegalArgumentException("Kích cỡ không tồn tại hoặc hiện không khả dụng."));

        List<Integer> normalizedToppingIds = toppingIds == null ? List.of() : toppingIds.stream()
                .map(id -> {
                    if (id == null || id <= 0) {
                        throw new IllegalArgumentException("Topping được chọn không hợp lệ.");
                    }
                    return id;
                })
                .distinct()
                .sorted()
                .toList();

        List<Topping> selectedToppings = normalizedToppingIds.stream()
                .map(id -> toppingRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Một topping được chọn không tồn tại.")))
                .toList();
        List<Integer> requiredToppingIds = toppingRepository.findAll().stream()
                .filter(topping -> Boolean.TRUE.equals(topping.getTrangThai()))
                .map(Topping::getIdTopping)
                .toList();
        if (!normalizedToppingIds.containsAll(requiredToppingIds)) {
            throw new IllegalArgumentException("Vui lòng giữ lại các topping bắt buộc.");
        }

        BigDecimal basePrice = toMoney(product.getGiaCoBan(), "Giá sản phẩm");
        BigDecimal sizeExtra = toMoney(size.getPhuThuSize(), "Phụ thu kích cỡ");
        List<CartTopping> cartToppings = selectedToppings.stream()
                .map(topping -> new CartTopping(
                        topping.getIdTopping(),
                        displayName(topping.getTenTopping(), "Topping"),
                        toMoney(topping.getGiaBan(), "Giá topping")))
                .toList();
        CartItem newItem = new CartItem(product.getIdSanPham(), displayName(product.getTenSanPham(), "Sản phẩm"),
                product.getHinhAnh(), size.getIdSize(), displayName(size.getTenSize(), "Kích cỡ"),
                basePrice, sizeExtra, cartToppings, quantity);
        String key = createKey(productId, sizeId, normalizedToppingIds);

        Map<String, CartItem> cart = getCart(session);
        CartItem existingItem = cart.get(key);
        if (existingItem == null) {
            cart.put(key, newItem);
        } else {
            try {
                existingItem.setQuantity(Math.addExact(existingItem.getQuantity(), quantity));
            } catch (ArithmeticException exception) {
                throw new IllegalArgumentException("Số lượng sản phẩm vượt quá giới hạn.");
            }
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateQuantity(HttpSession session, String key, Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Số lượng không được là số âm.");
        }
        if (quantity == 0) {
            removeItem(session, key);
            return;
        }
        CartItem item = findItem(session, key);
        item.setQuantity(quantity);
        session.setAttribute(CART_SESSION_KEY, getCart(session));
    }

    public void removeItem(HttpSession session, String key) {
        Map<String, CartItem> cart = getCart(session);
        if (key == null || cart.remove(key) == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng.");
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    private CartItem findItem(HttpSession session, String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Dòng sản phẩm không hợp lệ.");
        }
        CartItem item = getCart(session).get(key);
        if (item == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng.");
        }
        return item;
    }

    private Map<String, CartItem> validateCart(Object value) {
        if (!(value instanceof Map<?, ?> rawCart)) {
            throw new IllegalStateException("Dữ liệu giỏ hàng trong session không hợp lệ.");
        }
        for (Map.Entry<?, ?> entry : rawCart.entrySet()) {
            if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof CartItem)) {
                throw new IllegalStateException("Dữ liệu giỏ hàng trong session không hợp lệ.");
            }
        }
        @SuppressWarnings("unchecked")
        Map<String, CartItem> cart = (Map<String, CartItem>) rawCart;
        return cart;
    }

    private String createKey(Integer productId, Integer sizeId, List<Integer> toppingIds) {
        return productId + ":" + sizeId + ":" + toppingIds.stream()
                .sorted(Comparator.naturalOrder())
                .map(String::valueOf)
                .reduce((first, second) -> first + "," + second)
                .orElse("");
    }

    private BigDecimal toMoney(double value, String fieldName) {
        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ.");
        }
        return BigDecimal.valueOf(value);
    }

    private String displayName(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
