package com.bookreviewplatform.userservice.repository;

import com.bookreviewplatform.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on {@link UserEntity}.
 *
 * <p>This interface extends {@link JpaRepository}, providing out-of-the-box methods
 * such as {@code findById()}, {@code save()}, {@code delete()}, {@code findAll()}, etc.,
 * with {@link UUID} as the primary key type.</p>
 *
 * <p>Additionally, it defines a custom query method to retrieve a user by their unique email address.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Finds a user by their email address.
     *
     * <p>Email is treated as a unique identifier in the system (enforced at database level),
     * making this method ideal for authentication, password recovery, and duplicate checks.</p>
     *
     * @param email the email address to search for (case-sensitive as per database collation)
     * @return an {@link Optional} containing the {@link UserEntity} if found,
     *         or {@link Optional#empty()} if no user exists with the given email
     */
    Optional<UserEntity> findByEmail(String email);
}