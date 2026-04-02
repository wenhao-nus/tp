package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnsetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnsetCommand object.
 */
public class UnsetCommandParser implements Parser<UnsetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnsetCommand
     * and returns an UnsetCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnsetCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TELEGRAM, PREFIX_TAG);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TELEGRAM, PREFIX_TAG);

        List<Prefix> presentPrefixes = Stream.of(
                        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TELEGRAM, PREFIX_TAG)
                .filter(prefix -> argMultimap.getValue(prefix).isPresent())
                .toList();

        if (presentPrefixes.isEmpty()) {
            throw new ParseException(UnsetCommand.MESSAGE_NOT_UNSET);
        }

        if (presentPrefixes.size() > 1) {
            throw new ParseException(UnsetCommand.MESSAGE_MULTIPLE_FIELDS);
        }

        Prefix fieldPrefix = presentPrefixes.get(0);
        if (PREFIX_NAME.equals(fieldPrefix)) {
            throw new ParseException(UnsetCommand.MESSAGE_NAME_CANNOT_BE_UNSET);
        }

        Optional<String> fieldValue = argMultimap.getValue(fieldPrefix);
        if (fieldValue.isPresent() && !fieldValue.get().isEmpty()) {
            throw new ParseException(UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
        }

        return new UnsetCommand(index, fieldPrefix);
    }
}
