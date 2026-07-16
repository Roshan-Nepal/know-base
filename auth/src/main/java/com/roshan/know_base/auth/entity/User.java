package com.roshan.know_base.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roshan.know_base.common.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@SQLRestriction("deleted_at IS NULL")
public class User extends SoftDeletableEntity {
    private String username;
    @JsonIgnore
    private String password;
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns =@JoinColumn(name = "role_id")

    )
    private Set<Role> roles = new HashSet<>();
}
