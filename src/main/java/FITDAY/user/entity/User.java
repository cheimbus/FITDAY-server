package FITDAY.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens = new ArrayList<>();

    @CreatedDate
    @Column(name = "createAt", updatable = false)
    private LocalDateTime createdAt;
}
