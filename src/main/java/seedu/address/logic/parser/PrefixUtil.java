package seedu.address.logic.parser;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains utility methods used for handling Prefix-related parsing tasks in the various Parser classes.
 */
public class PrefixUtil {

    /**
     * Checks for unsupported prefixes in the given argument string.
     *
     * @param args the input arguments string.
     * @param supportedPrefixes the set of supported prefixes for the command.
     * @param commandWord the keyword of the command.
     * @param messageUsage the usage message of the command.
     * @throws ParseException if any unsupported prefixes are found.
     */
    public static void checkUnsupportedPrefixes(String args, Set<Prefix> supportedPrefixes,
                String commandWord, String messageUsage) throws ParseException {
        requireAllNonNull(args, supportedPrefixes, commandWord, messageUsage);

        List<String> tokens = List.of(args.trim().split("\\s+"));
        Set<String> invalidPrefixes = new LinkedHashSet<>();

        for (String token : tokens) {
            if (token.matches("[a-zA-Z]+/.*") && !isSupportedPrefix(token, supportedPrefixes)) {
                String unsupportedPrefix = token.substring(0, token.indexOf('/') + 1);
                invalidPrefixes.add(unsupportedPrefix);
            }
        }

        if (!invalidPrefixes.isEmpty()) {
            String invalidPrefixesString = String.join(" ", invalidPrefixes);
            throw new ParseException(String.format(MESSAGE_INVALID_PREFIX,
                    invalidPrefixesString, commandWord, messageUsage));
        }
    }

    /**
     * Returns true if the token starts with one of the supported prefixes.
     */
    private static boolean isSupportedPrefix(String token, Set<Prefix> supportedPrefixes) {
        return supportedPrefixes.stream().anyMatch(p -> token.startsWith(p.getPrefix()));
    }
}
