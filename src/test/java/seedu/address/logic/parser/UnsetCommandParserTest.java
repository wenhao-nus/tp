package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnsetCommand;

public class UnsetCommandParserTest {

    private static final String PHONE_EMPTY = " " + PREFIX_PHONE;
    private static final String EMAIL_EMPTY = " " + PREFIX_EMAIL;
    private static final String ADDRESS_EMPTY = " " + PREFIX_ADDRESS;
    private static final String TELEGRAM_EMPTY = " " + PREFIX_TELEGRAM;
    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE);

    private final UnsetCommandParser parser = new UnsetCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, TELEGRAM_EMPTY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1", UnsetCommand.MESSAGE_NOT_UNSET);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-1" + TELEGRAM_EMPTY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + TELEGRAM_EMPTY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_nameField_failure() {
        assertParseFailure(parser, "1 " + PREFIX_NAME, UnsetCommand.MESSAGE_NAME_CANNOT_BE_UNSET);
        assertParseFailure(parser, "1 " + PREFIX_NAME + "Alice", UnsetCommand.MESSAGE_NAME_CANNOT_BE_UNSET);
    }

    @Test
    public void parse_nonEmptyFieldValue_failure() {
        assertParseFailure(parser, "1 " + PREFIX_PHONE + "98765432", UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
        assertParseFailure(parser, "1 " + PREFIX_EMAIL + "alice@example.com",
                UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
        assertParseFailure(parser, "1 " + PREFIX_ADDRESS + "Somewhere",
                UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
        assertParseFailure(parser, "1 " + PREFIX_TELEGRAM + "@alice",
                UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
        assertParseFailure(parser, "1 " + PREFIX_TAG + "friend", UnsetCommand.MESSAGE_FIELD_VALUE_NOT_ALLOWED);
    }

    @Test
    public void parse_singleFieldSpecified_success() {
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + PHONE_EMPTY,
                new UnsetCommand(INDEX_FIRST_PERSON, PREFIX_PHONE));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + EMAIL_EMPTY,
                new UnsetCommand(INDEX_FIRST_PERSON, PREFIX_EMAIL));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + ADDRESS_EMPTY,
                new UnsetCommand(INDEX_FIRST_PERSON, PREFIX_ADDRESS));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + TELEGRAM_EMPTY,
                new UnsetCommand(INDEX_FIRST_PERSON, PREFIX_TELEGRAM));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + TAG_EMPTY,
                new UnsetCommand(INDEX_FIRST_PERSON, PREFIX_TAG));
    }

    @Test
    public void parse_multipleFields_failure() {
        assertParseFailure(parser, "1" + PHONE_EMPTY + EMAIL_EMPTY, UnsetCommand.MESSAGE_MULTIPLE_FIELDS);
        assertParseFailure(parser, "1" + TELEGRAM_EMPTY + TAG_EMPTY, UnsetCommand.MESSAGE_MULTIPLE_FIELDS);
    }

    @Test
    public void parse_repeatedField_failure() {
        assertParseFailure(parser, "1" + PHONE_EMPTY + PHONE_EMPTY, getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_EMPTY, getErrorMessageForDuplicatePrefixes(PREFIX_TAG));
    }
}
