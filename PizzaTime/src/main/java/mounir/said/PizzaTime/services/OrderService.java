package mounir.said.PizzaTime.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mounir.said.PizzaTime.models.Order;
import mounir.said.PizzaTime.repositories.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private final OrderRepository oRepo;

	public OrderService(OrderRepository repository) {
		this.oRepo = repository;
	}

	public List<Order> getAllOrders() {
		return this.oRepo.findAll();
	}

	public Order getOneOrder(Long id) {
		return this.oRepo.findById(id).orElse(null);
	}

	public Order createOrder(Order order) {
		return this.oRepo.save(order);
	}

	public Order editOrder(Order order) {
		return oRepo.save(order);
	}

	public String deleteOrder(Long id) {
		this.oRepo.deleteById(id);
		return "DELETED";
	}
}
