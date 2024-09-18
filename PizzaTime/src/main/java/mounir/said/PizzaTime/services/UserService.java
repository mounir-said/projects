package mounir.said.PizzaTime.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import mounir.said.PizzaTime.models.*;
import mounir.said.PizzaTime.repositories.*;

@Service
public class UserService {
	private final UserRepository userRepo;
	private final OrderRepository orderRepo;

	public UserService(UserRepository userRepo, OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        return userRepo.save(user);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findUserById(Long id) {
    	Optional<User> u = userRepo.findById(id);
    	return u.orElse(null);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepo.findByEmail(email);
        if(user == null) {
            return false;
        } else {
            return BCrypt.checkpw(password, user.getPassword());
        }
    }

    public List<Order> getAllOrdersByUser(Long id) {
        User user = userRepo.findById(id).get();
        return user.getOrders();
    }

    public void favoriteOrderById(Long userId, Long orderId) {
    	User user = userRepo.findById(userId).get();
		Order order = orderRepo.findById(orderId).get();
    	if(user.getFavoriteOrders().contains(order)) {
    		user.getFavoriteOrders().remove(order);
    	} else {
    		user.getFavoriteOrders().add(order);
    	}
    	userRepo.save(user);
    }

    public void editUserById(Long userId, User user) {
    	User userToUpdate = userRepo.findById(userId).get();
    	userToUpdate = user;
    	userRepo.save(userToUpdate);
    }
}
