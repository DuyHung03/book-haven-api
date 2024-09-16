package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.CartDto;
import com.duyhung.bookstoreapi.dto.CartItemDto;
import com.duyhung.bookstoreapi.entity.*;
import com.duyhung.bookstoreapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    public CartDto addToCart(String userId, String bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<Cart> findCart = cartRepository.findByUserUserId(userId);
        Cart cart;
        if (findCart.isEmpty()) {
            cart = new Cart();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            cart.setUser(user);
            cartRepository.save(cart);
        } else {
            cart = findCart.get();
        }

        Optional<CartItem> findBookInCartItems = cartItemRepository.findByBookBookIdAndCartCartId(bookId, cart.getCartId());
        if (findBookInCartItems.isPresent()) {
            //update quantity
            CartItem updateCartItem = findBookInCartItems.get();
            updateCartItem.setQuantity(findBookInCartItems.get().getQuantity() + quantity);

            cartItemRepository.save(updateCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        cartRepository.save(cart);

        return convertToDto(cart);
    }

    public CartDto getItemsFromCart(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().forEach(cartItem -> {
            if (cartItem.getQuantity() > cartItem.getBook().getInventory().getStock()) {
                cartItem.setQuantity(cartItem.getBook().getInventory().getStock());
            }
        });
        return convertToDto(cart);
    }

    public CartDto deleteItemFromCart(String bookId, String userId) {
        Cart findCart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartItem findBookInCart = cartItemRepository.findByBookBookIdAndCartCartId(bookId, findCart.getCartId())
                .orElseThrow(() -> new RuntimeException("Item in cart not found"));

        cartItemRepository.delete(findBookInCart);

        return convertToDto(findCart);
    }

    private CartDto convertToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        cartDto.setUserId(cart.getUser().getUserId());

        if (cart.getCartItems() != null) {
            cartDto.setCartItems(cart.getCartItems().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()));
        }

        return cartDto;
    }

    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);
        Inventory inventory = inventoryRepository.findByBookBookId(cartItem.getBook().getBookId())
                .orElseThrow(
                        () -> new RuntimeException("inventory not found")
                );
        cartItemDto.setBookId(cartItem.getBook().getBookId());
        cartItemDto.setTitle(cartItem.getBook().getTitle());
        cartItemDto.setAuthorName(cartItem.getBook().getAuthor().getAuthorName());
        cartItemDto.setPrice(cartItem.getBook().getPrice());
        cartItemDto.setInStock(inventory.getStock());
        cartItemDto.setImgUrls(cartItem.getBook().getImages().stream().map(BookImage::getImageUrl).collect(Collectors.toList()));
        return cartItemDto;
    }

    public CartDto increaseQuantity(String bookId, String userId) {
        Cart findCart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartItem findBookInCart = cartItemRepository.findByBookBookIdAndCartCartId(bookId, findCart.getCartId())
                .orElseThrow(() -> new RuntimeException("Item in cart not found"));
        Inventory findStock = inventoryRepository.findByBookBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (findBookInCart.getQuantity() < findStock.getStock()) {
            findBookInCart.setQuantity(findBookInCart.getQuantity() + 1);
            cartItemRepository.save(findBookInCart);
        } else {
            throw new RuntimeException("Quantity in stock is maximum");
        }

        return convertToDto(findCart);

    }

    public CartDto decreaseQuantity(String bookId, String userId) {
        Cart findCart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartItem findBookInCart = cartItemRepository.findByBookBookIdAndCartCartId(bookId, findCart.getCartId())
                .orElseThrow(() -> new RuntimeException("Item in cart not found"));
        Inventory findStock = inventoryRepository.findByBookBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (findBookInCart.getQuantity() > 1) {
            findBookInCart.setQuantity(findBookInCart.getQuantity() - 1);
            cartItemRepository.save(findBookInCart);
        } else {
            throw new RuntimeException("Quantity in stock is minimum");
        }

        return convertToDto(findCart);

    }

}
