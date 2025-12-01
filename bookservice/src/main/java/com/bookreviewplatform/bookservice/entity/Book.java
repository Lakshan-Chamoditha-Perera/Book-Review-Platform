package com.bookreviewplatform.bookservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * JPA entity representing a book in the central book catalog of the Book Review Platform.
 *
 * <p>This is the source of truth for all book metadata used across microservices
 * (Book Service, Review Service, future Recommendation/Analytics services).</p>
 *
 * <p>Uses UUID as primary key for security, scalability, and distributed system compatibility.
 * Title and author are mandatory and enforced at the database level.</p>
 *
 * <p>Designed with clean microservices principles:</p>
 * <ul>
 *   <li>No direct dependencies on other services' entities</li>
 *   <li>Minimal but essential fields — easily extendable</li>
 *   <li>Optimized for fast lookups and referential integrity</li>
 * </>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    /**
     * Unique identifier for the book.
     * Auto-generated UUID — globally unique, secure, and safe from enumeration attacks.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    /**
     * Title of the book.
     * Required field. Part of uniqueness constraint with author.
     */
    @Column(nullable = false, length = 500)
    private String title;

    /**
     * Name of the author(s).
     * Required field. Combined with title to enforce logical uniqueness
     * (e.g., prevents duplicate "Harry Potter" by "J.K. Rowling").
     */
    @Column(nullable = false, length = 200)
    private String author;
}