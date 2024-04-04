package spring.securitybasicv1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.securitybasicv1.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
