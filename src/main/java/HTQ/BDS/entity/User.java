package HTQ.BDS.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "_user")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false, unique = true, name = "user_names")
    @NotBlank(message = "Username cannot be blank")
    private String usernames;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(name = "profile_picture")
    private String profilePicture;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Roles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));
//        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return usernames;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
