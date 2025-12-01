package com.bookreviewplatform.reviewservice.repository;

import com.bookreviewplatform.reviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for performing CRUD and query operations on {@link Review} entities.
 *
 * <p>Extends {@link JpaRepository} to inherit standard methods like
 * {@code findById()}, {@code save()}, {@code delete()}, {@code findAll()}, etc.,
 * with {@link UUID} as the primary key type.</p>
 *
 * <p>Provides custom query methods to fetch reviews filtered by book or user,
 * which are essential for the core features of the Book Review Platform.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    /**
     * Retrieves all reviews for a specific book.
     *
     * <p>Used when displaying all reviews on a book's detail page
     * or calculating average rating.</p>
     *
     * @param bookId the UUID of the book
     * @return a {@link List} of reviews for the given book (may be empty)
     */
    List<Review> findByBookId(UUID bookId);

    /**
     * Retrieves all reviews written by a specific user.
     *
     * <p>Used when showing a user's review history or profile activity.</p>
     *
     * @param userId the UUID of the user
     * @return a {@link List} of reviews authored by the user (may be empty)
     */
    List<Review> findByUserId(UUID userId);
}