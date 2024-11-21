package com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.en.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Entity
@Table(name = "authorities")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authority")
    @Enumerated(value = EnumType.STRING)
    private RoleType authority;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(authority.name());
    }
    public static Role from(RoleType type) {
        var role = new Role();
        role.setAuthority(type);
        return role;
    }

}
