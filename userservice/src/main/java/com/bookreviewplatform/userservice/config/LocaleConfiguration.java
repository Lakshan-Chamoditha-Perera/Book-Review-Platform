package com.bookreviewplatform.userservice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Configuration class for internationalization (i18n) support.
 *
 * <p>
 * This configuration sets up Spring's i18n infrastructure to support multiple
 * languages
 * in the User Service. It configures message resolution from property files and
 * locale
 * detection from HTTP Accept-Language headers.
 * </p>
 *
 * <p>
 * Supported languages:
 * - English (en) - default
 * - Sinhala (si)
 * - Tamil (ta)
 * </p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Configuration
public class LocaleConfiguration {

    /**
     * Configures the MessageSource bean for resolving localized messages.
     *
     * <p>
     * This bean loads message properties from files matching the pattern
     * "i18/messages*.properties" in the classpath. Messages are cached in memory
     * for performance, and UTF-8 encoding is used to support Unicode characters
     * in Sinhala and Tamil translations.
     * </p>
     *
     * <p>
     * Configuration details:
     * - Basename: "i18/messages" (loads messages_en.properties,
     * messages_si.properties, etc.)
     * - Encoding: UTF-8 (supports Unicode characters)
     * - Fallback: Disabled (prevents falling back to system locale)
     * - Default locale: English
     * </p>
     *
     * @return configured MessageSource bean
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultLocale(Locale.ENGLISH);
        return messageSource;
    }

    /**
     * Configures the LocaleResolver bean for detecting user language preferences.
     *
     * <p>
     * This resolver extracts the locale from the HTTP Accept-Language header sent
     * by the client. If the header is missing or contains an unsupported locale,
     * it falls back to English.
     * </p>
     *
     * <p>
     * Supported locales:
     * - en (English) - default
     * - si (Sinhala)
     * - ta (Tamil)
     * </p>
     *
     * <p>
     * Example usage:
     * - Request with "Accept-Language: si" returns Sinhala messages
     * - Request with "Accept-Language: ta" returns Tamil messages
     * - Request with "Accept-Language: fr" falls back to English
     * - Request without header falls back to English
     * </p>
     *
     * @return configured LocaleResolver bean
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);

        List<Locale> supportedLocales = Arrays.asList(
                Locale.ENGLISH,
                new Locale("si"),
                new Locale("ta"));
        localeResolver.setSupportedLocales(supportedLocales);

        return localeResolver;
    }
}
