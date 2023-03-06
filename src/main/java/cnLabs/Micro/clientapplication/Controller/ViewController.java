package cnLabs.Micro.clientapplication.Controller;

import cnLabs.Micro.clientapplication.Clients.CartServiceClient;
import cnLabs.Micro.clientapplication.Clients.ItemServiceClient;
import cnLabs.Micro.clientapplication.Model.User;
import cnLabs.Micro.clientapplication.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ViewController {

    @Autowired
    ItemServiceClient itemServiceClient;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    CartServiceClient cartServiceClient;

    @GetMapping("/items")
    public String displayItems(Model model) {
        model.addAttribute("items", itemServiceClient.getAllItems());
        return "item-list";
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
    public ResponseEntity<?> addNewCartItem(@RequestParam Long itemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(cartServiceClient.addCartItem(itemId, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
