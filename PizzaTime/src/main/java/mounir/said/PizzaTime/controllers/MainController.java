package mounir.said.PizzaTime.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import mounir.said.PizzaTime.models.Order;
import mounir.said.PizzaTime.models.User;
import mounir.said.PizzaTime.services.OrderService;
import mounir.said.PizzaTime.services.UserService;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * Redirects to home or authentication page based on session state.
     */
    @GetMapping("/")
    public String landingPage(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }

    /**
     * Displays the login page.
     */
    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/home";
        }
        return "login.jsp";  // Returns login.jsp
    }

    /**
     * Displays the registration page.
     */
    @GetMapping("/register")
    public String registerForm(@ModelAttribute("user") User user, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/home";
        }
        return "registration.jsp";  // Returns registration.jsp
    }

    /**
     * Registers a new user and handles validation errors.
     */
    @PostMapping("/registration")
    public String registerUser(Model model, @Valid @ModelAttribute("user") User user, BindingResult result,
                               HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("register_error", "Couldn't register user. Please try again.");
            return "registration.jsp";  // Returns registration.jsp
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("register_error", "That email is already in use. Please try again.");
            return "registration.jsp";  // Returns registration.jsp
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            model.addAttribute("register_error", "Passwords don't match. Please try again.");
            return "registration.jsp";  // Returns registration.jsp
        }
        User u = userService.registerUser(user);
        session.setAttribute("userId", u.getId());
        return "redirect:/home";
    }

    /**
     * Authenticates and logs in the user.
     */
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password,
                            Model model, HttpSession session) {
        if (userService.authenticateUser(email, password)) {
            session.setAttribute("userId", userService.findByEmail(email).getId());
            return "redirect:/home";
        }
        model.addAttribute("login_error", "User authentication error. Please try again.");
        return "login.jsp";  // Returns login.jsp
    }

    /**
     * Logs out the user and invalidates the session.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /**
     * Displays the user's dashboard.
     */
    @GetMapping("/home")
    public String dashboard(Model model, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", currentUserId);
        return "Home.jsp";
    }

    /**
     * Displays the user's account details and orders.
     */
    @GetMapping("/account/{id}")
    public String account(Model model, HttpSession session, @PathVariable("id") Long id) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", currentUserId);
        List<Order> pastOrders = userService.getAllOrdersByUser(id);
        List<Order> favoriteOrders = userService.findUserById(id).getFavoriteOrders();
        for (Order order : favoriteOrders) {
            pastOrders.remove(order);
        }
        model.addAttribute("user", userService.findUserById(currentUserId));
        model.addAttribute("pastOrders", pastOrders);
        model.addAttribute("favoriteOrders", favoriteOrders);
        return "Account.jsp";
    }

    /**
     * Edits the user's account information.
     */
    @PutMapping("/editaccount/{id}")
    public String editAccount(HttpSession session, @PathVariable("id") Long userId,
                              @Valid @ModelAttribute("user") User user, BindingResult result) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        userService.editUserById(currentUserId, user);
        return "redirect:/account/" + currentUserId;
    }

    /**
     * Adds an order to the user's list of favorites.
     */
    @GetMapping("/favorite/{id}")
    public String favoriteAnOrder(@PathVariable("id") Long orderId, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        userService.favoriteOrderById(currentUserId, orderId);
        return "redirect:/account/" + currentUserId;
    }

    /**
     * Displays the order creation form.
     */
    @GetMapping("/order")
    public String orderForm(@ModelAttribute("order") Order order, Model model, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", currentUserId);
        model.addAttribute("user", userService.findUserById(currentUserId));
        return "CreateOrder.jsp";
    }

    /**
     * Creates a new order and redirects to checkout.
     */
    @PostMapping("/createorder")
    public String createOrder(Model model, HttpSession session, @Valid @ModelAttribute("order") Order order,
                              BindingResult result) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        Order currentOrder = orderService.createOrder(order);
        session.setAttribute("currentOrder", currentOrder);
        return "redirect:/checkout/" + currentOrder.getId();
    }

    /**
     * Displays the checkout page for the current order.
     */
    @GetMapping("/checkout/{id}")
    public String checkoutPage(Model model, @PathVariable("id") Long orderId, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        model.addAttribute("currentOrder", session.getAttribute("currentOrder"));
        Double price = 9.99 + currentOrder.getToppings().size() * 0.50;
        model.addAttribute("price", price);
        return "Checkout.jsp";
    }

    /**
     * Completes the purchase and redirects to the home page.
     */
    @PostMapping("/purchase/{id}")
    public String purchaseOrder(@PathVariable("id") Long id, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        session.removeAttribute("currentOrder");
        return "redirect:/home";
    }

    /**
     * Deletes an order and redirects to the order creation page.
     */
    @DeleteMapping("/checkout/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long id, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return "redirect:/";
        }
        orderService.deleteOrder(id);
        session.removeAttribute("currentOrder");
        return "redirect:/order";
    }
}
