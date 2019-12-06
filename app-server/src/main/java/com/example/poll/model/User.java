package com.example.poll.model;

import com.example.poll.model.audit.DateAudit;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

//    @NotBlank in oauth2 username can be abcent
    @Size(max = 30)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

//    @NotBlank in oauth2 password can be abcent
    @Size(max = 100)
    private String password;

    private String imageUrl;

    @NotNull
    // @Column(columnDefinition = "varchar(25)")  becuase java cannot get size of enum
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @ManyToMany(fetch = FetchType.EAGER) //Todo  Refactor to Lazy
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), // field which store link to owned class in child class or third table
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
