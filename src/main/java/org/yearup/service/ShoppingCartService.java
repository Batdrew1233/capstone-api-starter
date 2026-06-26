package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;


    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    /*
    Builds and returns the user's shopping cart.
     */
    public ShoppingCart getByUserId(int userId)
    {

        ShoppingCart cart = new ShoppingCart();

        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        for (CartItem cartItem : cartItems){
            Product product = productService.getById(cartItem.getProductId());

            if (product != null){
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setProduct(product);
                shoppingCartItem.setQuantity(cartItem.getQuantity());
                cart.add(shoppingCartItem);
            }
        }
        return cart;
    }

    /*
    Adds A product to the user's cart
    If the product is already in the cart, the quantity increases by one.
     */
    public ShoppingCart addProduct(int userId, int productId){
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem == null){
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(1);
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        shoppingCartRepository.save(cartItem);
        return getByUserId(userId);
    }

    /*
    Updates the quantity of a product already in the user's cart.
     */
    public ShoppingCart updateProduct(int userId, int productId, int quantity){
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null){
            cartItem.setQuantity(quantity);
            shoppingCartRepository.save(cartItem);
        }

        return getByUserId(userId);
    }

    /*
    Clears all items from the user's cart and returns the empty cart.
    Transactional makes sure the changes are saved but if
    an error occurs, spring rolls back transaction.
     */
    @Transactional
    public ShoppingCart clearCart(int userId){
        shoppingCartRepository.deleteByUserId(userId);

        return getByUserId(userId);
    }
}
