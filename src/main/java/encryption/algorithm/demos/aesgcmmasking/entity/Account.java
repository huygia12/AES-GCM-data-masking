package encryption.algorithm.demos.aesgcmmasking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; //bcrypt

    @Column(name = "encryption_key", nullable = false)
    private String key; // masking by master-key

    private String name; // masking

    private String address; // masking

    private String phone; // masking

    @Column(name="date_of_birth")
    private String dateOfBirth; // masking

    @Column(name="citizen_id")
    private String citizenID; // masking

    @Column(name="created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
