package mounir.said.PizzaTime.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mounir.said.PizzaTime.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    List<User> findAll();
    
    User findByEmail(String email);
}

