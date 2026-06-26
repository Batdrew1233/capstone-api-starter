package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{

    private ShoppingCartService shoppingCartService;
    private UserService userService;


    /*
    Injects the service needed to manage shopping cart operations
    Retrieves the currently logged-in user.
     */
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    /*
    Retrieves the shopping cart for the currently authenticated user.
     */
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        //Gets all items in the cart and return the cart
        return shoppingCartService.getByUserId(userId);
    }

    /*
    Adds the selected product to the authenticated user's shopping cart.
    Returns the updated cart with HTTP 201 Created.
     */
    @PostMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable int productId, Principal principal)
    {
        String userName = principal.getName();

        User user = userService.getByUserName(userName);
        int userId = user.getId();

        ShoppingCart cart = shoppingCartService.addProduct(userId, productId);

        return ResponseEntity.status(201).body(cart);
    }

    /*
    Updates the quantity of an existing product in the authenticated user's shopping.
     */
    @PutMapping("/products/{productId}")
    public ShoppingCart updateProductQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal){
        String userName = principal.getName();

        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return shoppingCartService.updateProduct(userId, productId, item.getQuantity());
    }

    /*
    Removes all products from the authenticated user's shopping cart.
     */
    @DeleteMapping
    public ShoppingCart clearCart(Principal principal){
        String userName = principal.getName();

        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return shoppingCartService.clearCart(userId);
    }
}
