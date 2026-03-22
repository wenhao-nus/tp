package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTelegramHandle(String)}
 */
public class Telegram {

    public static final String MESSAGE_CONSTRAINTS =
            "Telegram handles must be 5-32 characters long, can only contain alphanumeric characters or underscores, "
                    + "and can optionally start with '@'.";
    public static final String VALIDATION_REGEX = "^@?[a-zA-Z0-9_]{5,32}$";
    public static final String TELEGRAM_PREFIX = "@";

    public final String value;

    /**
     * Constructs a {@code Telegram}.
     *
     * @param telegramHandle A valid telegram handle.
     */
    public Telegram(String telegramHandle) {
        requireNonNull(telegramHandle);

        String cleanHandle = telegramHandle;

        if (!cleanHandle.equals("-") && cleanHandle.startsWith("@")) {
            cleanHandle = cleanHandle.substring(1);
        }

        checkArgument(isValidTelegramHandle(cleanHandle), MESSAGE_CONSTRAINTS);

        this.value = cleanHandle;
    }

    /**
     * Returns true if a given string is a valid telegramHandle.
     */
    public static boolean isValidTelegramHandle(String test) {
        return test.equals("-") || test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Telegram)) {
            return false;
        }

        Telegram otherTelegram = (Telegram) other;
        return value.equalsIgnoreCase(otherTelegram.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return value;
    }

}
