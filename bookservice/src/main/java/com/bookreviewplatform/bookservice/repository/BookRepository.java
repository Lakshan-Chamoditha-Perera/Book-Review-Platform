package com.bookreviewplatform.bookservice.repository;

import com.bookreviewplatform.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on {@link Book} entities
 * in the Book Service.
 *
 * <p>Extends {@link JpaRepository} to inherit powerful built-in methods such as
 * {@code findById()}, {@code findAll()}, {@code save()}, {@code deleteById()},
 * {@code existsById()}, etc., with {@link UUID} as the primary key type.</p>
 *
 * <p>Currently provides all necessary functionality via inherited methods.
 * Custom query methods can be added in the future as requirements evolve
 * (e.g., search by title, author, ISBN).</p>
 *
 * <p>Used by {@code BookService} to interact with the underlying database
 * in a clean, type-safe, and repository-pattern-compliant way.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    /**
     * Finds a book by its title and author.
     * Useful for preventing duplicates during creation.
     *
     * @param title  the book title (case-sensitive)
     * @param author the author name(s)
     * @return an {@link Optional} containing the book if found, empty otherwise
     */
    Optional<Book> findByTitleAndAuthor(String title, String author);

    /**
     * Checks whether a book with the given title and author already exists.
     * Supports enforcement of logical uniqueness (title + author combination).
     *
     * @param title  the book title
     * @param author the author name(s)
     * @return true if a matching book exists, false otherwise
     */
    boolean existsByTitleAndAuthor(String title, String author);
}