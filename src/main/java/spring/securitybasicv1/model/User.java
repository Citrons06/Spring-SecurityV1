package spring.securitybasicv1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class User {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    @CreationTimestamp
    private Timestamp createDate;
}
