package cnLabs.Micro.clientapplication.Controller;

import cnLabs.Micro.clientapplication.Clients.CartServiceClient;
import cnLabs.Micro.clientapplication.Clients.ItemServiceClient;
import cnLabs.Micro.clientapplication.Model.*;
import cnLabs.Micro.clientapplication.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    @Autowired
    ItemServiceClient itemServiceClient;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    CartServiceClient cartServiceClient;

    @GetMapping("/items")
    public String displayItems(Model model, Authentication authentication) {
        model.addAttribute("items", itemServiceClient.getAllItems());

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Optional<Cart> cartOptional = Optional.ofNullable(cartServiceClient.getCartByUserId(user.getId()));
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            List<CartItem> cartItems = cart.getItems();
            Map<Long, Integer> cartItemMap = cartItems.stream().collect(
                    Collectors.toMap(CartItem::getItemId, CartItem::getAmount));
            model.addAttribute("itemsInCart", cartItemMap);
        }

        return "item-list";
    }

    @GetMapping("/cart")
    public String displayCart(Model model, Authentication authentication) {
        List<Item> items = itemServiceClient.getAllItems();
        Map<Long, String> itemMap = items.stream().collect(
                Collectors.toMap(Item::getId, Item::getName));
        model.addAttribute("itemsMap", itemMap);

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Optional<Cart> cartOptional = Optional.ofNullable(cartServiceClient.getCartByUserId(user.getId()));
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            List<CartItem> cartItems = cart.getItems();
            model.addAttribute("itemsInCartList", cartItems);
        }

        return "cart";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/register";
        }
        myUserDetailsService.registerUser(user);
        return "redirect:/items";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/cart")
    public String addNewCartItem(@RequestParam Long itemId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        try {
            cartServiceClient.addCartItem(itemId, user.getId());
            return "redirect:/items";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Not Found", e);
        }
    }

    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam Long itemId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        try {
            cartServiceClient.removeCartItem(itemId, user.getId());
            return "redirect:/items";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Not Found", e);
        }
    }

}
