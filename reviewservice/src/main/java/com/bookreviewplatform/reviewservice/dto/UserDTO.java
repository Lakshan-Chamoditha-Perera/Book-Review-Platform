package com.bookreviewplatform.reviewservice.dto;

import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object representing minimal user information
 * received from the <strong>User Service</strong> via inter-service communication.
 *
 * <p>This DTO is used within the Review Service to:</p>
 * <ul>
 *   <li>Validate that a {@code userId} in a review request belongs to a real user</li>
 *   <li>Display reviewer names in review listings (e.g., "Review by @john_doe")</li>
 *   <li>Avoid tight coupling with the full UserEntity from the User Service</li>
 * </ul>
 *
 * <p>Only essential, non-sensitive fields are included to respect privacy
 * and keep inter-service payloads lightweight.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * Unique identifier of the user in the User Service.
     * Matches the {@code userId} referenced in reviews.
     */
    private UUID id;

    /**
     * Public username chosen by the user.
     * Used for display purposes (e.g., "Reviewed by <strong>username</strong>").
     */
    private String username;

    /**
     * User's email address.
     * Included for potential admin/moderation use cases or future enhancements
     * (e.g., sending notifications). Never exposed publicly without consent.
     */
    private String email;
}