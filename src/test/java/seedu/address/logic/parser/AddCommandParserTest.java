package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX;
import static seedu.address.logic.Messages.MESSAGE_PREAMBLE_NOT_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_PLACEHOLDER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TELEGRAM_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB)
                .withTelegram(VALID_TELEGRAM_BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TELEGRAM_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .withTelegram(VALID_TELEGRAM_BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));

    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TELEGRAM_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY
                        + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_PHONE, PREFIX_TELEGRAM));

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags, missing phone, address
        Person expectedPerson = new PersonBuilder(AMY)
                .withoutPhone()
                .withoutAddress()
                .withoutTelegram()
                .withTags()
                .build();
        String userInput = NAME_DESC_AMY + EMAIL_DESC_AMY;
        assertParseSuccess(parser, userInput,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_unsupportedPrefixes_throwsParseException() {
        // single unsupported prefix
        String input1 = "n/James Ho e/james@example.com tele/@JamesHo";
        String expectedMessageSingle = String.format(MESSAGE_INVALID_PREFIX, "tele/",
                AddCommand.COMMAND_WORD, AddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, input1, expectedMessageSingle);

        // multiple unsupported prefixes
        String input2 = "n/John e/john@example.com w/2 L/label";
        String expectedMessageMultiple = String.format(MESSAGE_INVALID_PREFIX, "w/ L/",
                AddCommand.COMMAND_WORD, AddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, input2, expectedMessageMultiple);

        // duplicate unsupported prefix
        String input3 = "n/James Ho e/james@example.com c/course_1 c/course_2";
        String expectedMessageDuplicate = String.format(MESSAGE_INVALID_PREFIX, "c/",
                AddCommand.COMMAND_WORD, AddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, input3, expectedMessageDuplicate);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing name only
        String expectedMessageMissingName = AddCommand.MESSAGE_MISSING_PARAMS
                + AddCommand.MESSAGE_MISSING_NAME + "\n" + AddCommand.MESSAGE_USAGE;

        assertParseFailure(parser, PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessageMissingName);

        // missing email only
        String expectedMessageMissingEmail = AddCommand.MESSAGE_MISSING_PARAMS
                + AddCommand.MESSAGE_MISSING_EMAIL + "\n" + AddCommand.MESSAGE_USAGE;

        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessageMissingEmail);

        // missing both name and email
        String expectedMessageMissingBoth = AddCommand.MESSAGE_MISSING_PARAMS
                + AddCommand.MESSAGE_MISSING_NAME + " and "
                + AddCommand.MESSAGE_MISSING_EMAIL + "\n" + AddCommand.MESSAGE_USAGE;

        assertParseFailure(parser, PHONE_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessageMissingBoth);
    }

    @Test
    public void parse_preambleNotEmpty_failure() {
        String preamble = "unexpectedText ";
        String validArgs = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB;
        String expectedMessage = String.format(MESSAGE_PREAMBLE_NOT_EMPTY, AddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, preamble + validArgs, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // placeholder address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_PLACEHOLDER_DESC
                + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TELEGRAM_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);
    }
}
