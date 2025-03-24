package encryption.algorithm.demos.aesgcmmasking.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AccountDTO(
    Long id,
    String email,
    String name,
    String address,
    String phone,
    String dateOfBirth,
    String citizenID,
    LocalDateTime createdAt) {
}
