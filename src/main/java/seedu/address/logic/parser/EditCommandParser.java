package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private static final Set<Prefix> SUPPORTED_PREFIXES = Set.of(
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TELEGRAM, PREFIX_TAG
    );

    /**
     * Functional interface to represent a function that converts a string into object of type T.
     */
    @FunctionalInterface
    private interface StringParser<T> {
        // Solution below inspired by https://stackoverflow.com/a/73036423.
        T parse(String input) throws ParseException;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        checkEmptyArgs(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, SUPPORTED_PREFIXES.toArray(Prefix[]::new));

        checkUnsupportedPrefixes(args);

        Index index = checkAndParseIndex(argMultimap.getPreamble());

        // Checks if duplicate prefixes are present
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_TELEGRAM);

        EditPersonDescriptor editPersonDescriptor = createEditPersonDescriptor(argMultimap);

        // Checks if any fields to edit are provided
        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCommand.MESSAGE_NOT_EDITED + "\n" + EditCommand.MESSAGE_USAGE));
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Checks if the arguments are empty or contain only whitespace.
     *
     * @throws ParseException if no input is provided.
     */
    private void checkEmptyArgs(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCommand.MESSAGE_INDEX_AND_PREFIX_MISSING + "\n" + EditCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Checks whether any unsupported prefixes are present in the arguments.
     *
     * @throws ParseException if any unsupported prefix is found.
     */
    private void checkUnsupportedPrefixes(String args) throws ParseException {
        PrefixUtil.checkUnsupportedPrefixes(args, SUPPORTED_PREFIXES,
                EditCommand.COMMAND_WORD, EditCommand.MESSAGE_USAGE);
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
                    MESSAGE_INVALID_INDEX + "\n" + EditCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Creates an {@code EditPersonDescriptor} from the provided {@code ArgumentMultimap}.
     *
     * @throws ParseException if any provided field values to edit are invalid.
     */
    private EditPersonDescriptor createEditPersonDescriptor(ArgumentMultimap argMultimap) throws ParseException {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        setFieldIfPresent(argMultimap.getValue(PREFIX_NAME),
                editPersonDescriptor::setName, ParserUtil::parseName);

        setFieldIfPresent(argMultimap.getValue(PREFIX_PHONE),
                editPersonDescriptor::setPhone, ParserUtil::parsePhone);

        setFieldIfPresent(argMultimap.getValue(PREFIX_EMAIL),
                editPersonDescriptor::setEmail, ParserUtil::parseEmail);

        setFieldIfPresent(argMultimap.getValue(PREFIX_ADDRESS),
                editPersonDescriptor::setAddress, ParserUtil::parseAddress);

        setFieldIfPresent(argMultimap.getValue(PREFIX_TELEGRAM),
                editPersonDescriptor::setTelegram, ParserUtil::parseTelegram);

        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        return editPersonDescriptor;
    }

    /**
     * Sets a field in the {@code EditPersonDescriptor} if a value is present.
     * If {@code Optional<String> value} is present, it will be parsed and passed into the updater.
     */
    private <T> void setFieldIfPresent(Optional<String> value, Consumer<T> fieldUpdater,
            StringParser<T> parser) throws ParseException {

        if (value.isPresent()) {
            fieldUpdater.accept(parser.parse(value.get()));
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * Validation of invalid or empty tags is handled in parseTags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseTags(tags));
    }

}
