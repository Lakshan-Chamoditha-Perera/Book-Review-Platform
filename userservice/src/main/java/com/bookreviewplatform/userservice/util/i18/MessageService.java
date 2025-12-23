package com.bookreviewplatform.userservice.util.i18;

import java.util.Locale;

/**
 * Service interface for resolving localized messages.
 *
 * <p>
 * This service provides a centralized way to resolve message keys to localized
 * strings
 * based on the user's language preference. It supports parameterized messages
 * and
 * explicit locale specification.
 * </p>
 *
 * <p>
 * The service automatically detects the current request's locale from the
 * Accept-Language
 * header and resolves messages accordingly. If a message key is not found in
 * the requested
 * locale, it falls back to English.
 * </p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
public interface MessageService {

    /**
     * Resolves a message key to a localized string based on the current request
     * locale.
     *
     * <p>
     * The locale is automatically detected from the Accept-Language header of the
     * current HTTP request. If no locale is found, English is used as the default.
     * </p>
     *
     * @param key The message key (e.g., "user.not.found")
     * @return The localized message
     */
    String getMessage(String key);

    /**
     * Resolves a parameterized message key with arguments.
     *
     * <p>
     * This method supports messages with placeholders like {0}, {1}, etc.
     * The arguments are inserted into the message in the order they appear.
     * </p>
     *
     * <p>
     * Example:
     * 
     * <pre>
     * // messages_en.properties: user.not.found.with.id=User not found with ID:
     * // {0}
     * String message = messageService.getMessage("user.not.found.with.id", 123);
     * // Returns: "User not found with ID: 123"
     * </pre>
     * </p>
     *
     * @param key  The message key
     * @param args Arguments to be inserted into the message
     * @return The localized message with parameters replaced
     */
    String getMessage(String key, Object... args);

    /**
     * Resolves a message for a specific locale (overrides request locale).
     *
     * <p>
     * This method allows explicit locale specification, bypassing the automatic
     * locale detection from the request. Useful for system-generated messages
     * or when you need to generate messages in multiple languages.
     * </p>
     *
     * @param key    The message key
     * @param locale The target locale
     * @return The localized message
     */
    String getMessage(String key, Locale locale);

    /**
     * Resolves a parameterized message for a specific locale.
     *
     * <p>
     * Combines explicit locale specification with parameterized message support.
     * </p>
     *
     * @param key    The message key
     * @param locale The target locale
     * @param args   Arguments to be inserted into the message
     * @return The localized message with parameters replaced
     */
    String getMessage(String key, Locale locale, Object... args);
}
