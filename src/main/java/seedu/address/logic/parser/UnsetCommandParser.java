package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Collections;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.UnsetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnsetCommand object
 */
public class UnsetCommandParser implements Parser<UnsetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnsetCommand
     * and returns an UnsetCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public UnsetCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_TELEGRAM);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE), pe);
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException("Name cannot be unset.");
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(Optional.empty());
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(Optional.empty());
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(Optional.empty());
        }
        if (argMultimap.getValue(PREFIX_TELEGRAM).isPresent()) {
            editPersonDescriptor.setTelegram(Optional.empty());
        }
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            editPersonDescriptor.setTags(Collections.emptySet());
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(UnsetCommand.MESSAGE_NOT_UNSET);
        }

        return new UnsetCommand(index, editPersonDescriptor);
    }
}
