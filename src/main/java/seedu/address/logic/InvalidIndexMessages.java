package seedu.address.logic;

/**
 * Container for user-visible messages, specifically for invalid index errors.
 */
public class InvalidIndexMessages {

    public static final String MESSAGE_MISSING_INDEX = "Index must be provided.";
    public static final String MESSAGE_MULTIPLE_INDICES = "Only one index should be provided.";
    public static final String MESSAGE_INDEX_NON_NUMERIC = "Index cannot be non-numeric.";
    public static final String MESSAGE_INDEX_NON_INTEGER = "Index cannot be non-integer.";
    public static final String MESSAGE_INDEX_ZERO = "Index cannot be zero.";
    public static final String MESSAGE_INDEX_NEGATIVE = "Index cannot be negative.";
    public static final String MESSAGE_INDEX_OVERFLOW = "Index exceeds the range of valid integers.";
}
