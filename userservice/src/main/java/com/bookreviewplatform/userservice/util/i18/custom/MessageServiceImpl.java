package com.bookreviewplatform.userservice.util.i18.custom;

import com.bookreviewplatform.userservice.util.i18.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.logging.Logger;

/**
 * Implementation of the MessageService interface.
 *
 * <p>
 * This service resolves localized messages from property files using Spring's
 * MessageSource. It automatically detects the current request's locale from
 * LocaleContextHolder and provides fallback to English when messages are not
 * found.
 * </p>
 *
 * <p>
 * Thread Safety: This implementation is thread-safe. LocaleContextHolder uses
 * ThreadLocal storage, ensuring each request gets its own locale context.
 * </p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final Logger logger = Logger.getLogger(MessageServiceImpl.class.getName());
    private final MessageSource messageSource;

    /**
     * Resolves a message key to a localized string based on the current request
     * locale.
     *
     * <p>
     * The locale is obtained from LocaleContextHolder, which is populated by
     * Spring's LocaleResolver from the Accept-Language header.
     * </p>
     *
     * @param key The message key (e.g., "user.not.found")
     * @return The localized message
     */
    @Override
    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        logger.fine("Resolving message key: " + key + " for locale: " + locale);
        return messageSource.getMessage(key, null, locale);
    }

    /**
     * Resolves a parameterized message key with arguments.
     *
     * <p>
     * Arguments are inserted into the message at positions marked by {0}, {1}, etc.
     * The locale is automatically detected from the current request.
     * </p>
     *
     * @param key  The message key
     * @param args Arguments to be inserted into the message
     * @return The localized message with parameters replaced
     */
    @Override
    public String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        logger.fine("Resolving message key: " + key + " with " + args.length + " arguments for locale: " + locale);
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * Resolves a message for a specific locale (overrides request locale).
     *
     * <p>
     * This method bypasses the automatic locale detection and uses the provided
     * locale instead. Useful for generating messages in a specific language
     * regardless of the request context.
     * </p>
     *
     * @param key    The message key
     * @param locale The target locale
     * @return The localized message
     */
    @Override
    public String getMessage(String key, Locale locale) {
        logger.fine("Resolving message key: " + key + " for explicit locale: " + locale);
        return messageSource.getMessage(key, null, locale);
    }

    /**
     * Resolves a parameterized message for a specific locale.
     *
     * <p>
     * Combines explicit locale specification with parameterized message support.
     * Arguments are inserted at positions marked by {0}, {1}, etc.
     * </p>
     *
     * @param key    The message key
     * @param locale The target locale
     * @param args   Arguments to be inserted into the message
     * @return The localized message with parameters replaced
     */
    @Override
    public String getMessage(String key, Locale locale, Object... args) {
        logger.fine(
                "Resolving message key: " + key + " with " + args.length + " arguments for explicit locale: " + locale);
        return messageSource.getMessage(key, args, locale);
    }
}
