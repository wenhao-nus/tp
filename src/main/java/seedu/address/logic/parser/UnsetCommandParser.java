package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnsetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnsetCommand object.
 */
public class UnsetCommandParser implements Parser<UnsetCommand> {

    private static final Set<Prefix> SUPPORTED_PREFIXES = Set.of(
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
            PREFIX_TELEGRAM, PREFIX_TAG, PREFIX_COURSE, PREFIX_TUTORIAL
    );

    /**
     * Parses the given {@code String} of arguments in the context of the UnsetCommand
     * and returns an UnsetCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnsetCommand parse(String args) throws ParseException {
        requireNonNull(args);
        checkEmptyArgs(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, SUPPORTED_PREFIXES.toArray(Prefix[]::new));

        checkUnsupportedPrefixes(args);

        Index index = checkAndParseIndex(argMultimap.getPreamble());

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TELEGRAM, PREFIX_TAG);

        List<Prefix> presentPrefixes = SUPPORTED_PREFIXES.stream()
                .filter(prefix -> argMultimap.getValue(prefix).isPresent())
                .toList();

        if (presentPrefixes.isEmpty()) {
            throw new ParseException(UnsetCommand.MESSAGE_NOT_UNSET);
        }

        for (Prefix prefix : presentPrefixes) {
            if (PREFIX_NAME.equals(prefix)) {
                throw new ParseException(UnsetCommand.MESSAGE_NAME_CANNOT_BE_UNSET);
            }
            if (PREFIX_EMAIL.equals(prefix)) {
                throw new ParseException(UnsetCommand.MESSAGE_EMAIL_CANNOT_BE_UNSET);
            }
            if (PREFIX_COURSE.equals(prefix) || PREFIX_TUTORIAL.equals(prefix)) {
                throw new ParseException(UnsetCommand.MESSAGE_TUTINFO_CANNOT_BE_UNSET);
            }
        }

        if (presentPrefixes.size() > 1) {
            throw new ParseException(UnsetCommand.MESSAGE_MULTIPLE_FIELDS);
        }

        Prefix fieldPrefix = presentPrefixes.get(0);
        Optional<String> fieldValue = argMultimap.getValue(fieldPrefix);
        if (fieldValue.isPresent() && !fieldValue.get().isEmpty()) {
            throw new ParseException(UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
        }

        return new UnsetCommand(index, fieldPrefix);
    }

    /**
     * Checks if the arguments are empty or contain only whitespace.
     *
     * @throws ParseException if no input is provided.
     */
    private void checkEmptyArgs(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnsetCommand.MESSAGE_INDEX_AND_PREFIX_MISSING + "\n" + UnsetCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Checks whether any unsupported prefixes are present in the arguments.
     *
     * @throws ParseException if any unsupported prefix is found.
     */
    private void checkUnsupportedPrefixes(String args) throws ParseException {
        PrefixUtil.checkUnsupportedPrefixes(args, SUPPORTED_PREFIXES,
                UnsetCommand.COMMAND_WORD, UnsetCommand.MESSAGE_USAGE);
    }

    /**
     * Checks and parses the preamble string into an index.
     *
     * @throws ParseException if the index is missing or invalid.
     */
    private Index checkAndParseIndex(String preamble) throws ParseException {
        try {
            return ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_INVALID_INDEX + "\n" + UnsetCommand.MESSAGE_USAGE), pe);
        }
    }

}
