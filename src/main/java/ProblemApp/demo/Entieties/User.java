package ProblemApp.demo.Entieties;

import ProblemApp.demo.Enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password_hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(){}

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public User(String email, String login, Role role) {
        this.email = email;
        this.login = login;
        this.role = role;


    }
}
