package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        PersonMatchesKeywordsPredicate expectedPredicate = new PersonMatchesKeywordsPredicate(
                Arrays.asList("Alice", "Bob"),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        FindCommand expectedFindCommand = new FindCommand(expectedPredicate);

        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob", expectedFindCommand);

        assertParseSuccess(parser, " \n " + PREFIX_NAME
                + "Alice \n \t " + PREFIX_NAME + "Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validNonNameArgs_returnsFindCommand() {
        PersonMatchesKeywordsPredicate expectedPhonePredicate = new PersonMatchesKeywordsPredicate(
                Collections.emptyList(),
                Arrays.asList("91234567"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        assertParseSuccess(parser, " " + PREFIX_PHONE + "91234567", new FindCommand(expectedPhonePredicate));

        PersonMatchesKeywordsPredicate expectedEmailPredicate = new PersonMatchesKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(), Arrays.asList("alice@example.com"), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        assertParseSuccess(parser, " " + PREFIX_EMAIL + "alice@example.com", new FindCommand(expectedEmailPredicate));

        PersonMatchesKeywordsPredicate expectedAddressPredicate = new PersonMatchesKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Arrays.asList("Jurong West"),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "Jurong West", new FindCommand(expectedAddressPredicate));

        PersonMatchesKeywordsPredicate expectedTagPredicate = new PersonMatchesKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Arrays.asList("friends"), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        assertParseSuccess(parser, " " + PREFIX_TAG + "friends", new FindCommand(expectedTagPredicate));

        PersonMatchesKeywordsPredicate expectedTelegramPredicate = new PersonMatchesKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Arrays.asList("alice_handle"), Collections.emptyList(), Collections.emptyList()
        );
        assertParseSuccess(parser, " " + PREFIX_TELEGRAM + "alice_handle", new FindCommand(expectedTelegramPredicate));

        PersonMatchesKeywordsPredicate expectedCourseTutorialPredicate = new PersonMatchesKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Arrays.asList("T01"), Arrays.asList("CS2103T")
        );
        assertParseSuccess(parser, " " + PREFIX_COURSE + "CS2103T " + PREFIX_TUTORIAL + "T01",
                new FindCommand(expectedCourseTutorialPredicate));
    }

    @Test
    public void parse_tutorialWithoutCourse_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_TUTORIAL + "T01", FindCommand.MESSAGE_TUTORIAL_REQUIRES_COURSE);
    }

    @Test
    public void parse_emptyPrefixValue_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " " + PREFIX_NAME, expectedMessage);
        assertParseFailure(parser, " " + PREFIX_PHONE, expectedMessage);
        assertParseFailure(parser, " " + PREFIX_EMAIL, expectedMessage);
        assertParseFailure(parser, " " + PREFIX_ADDRESS, expectedMessage);
        assertParseFailure(parser, " " + PREFIX_TAG, expectedMessage);
        assertParseFailure(parser, " " + PREFIX_TELEGRAM, expectedMessage);
        assertParseFailure(parser, " " + PREFIX_COURSE + " " + PREFIX_TUTORIAL + "T01", expectedMessage);
        assertParseFailure(parser, " " + PREFIX_COURSE + "CS2103T " + PREFIX_TUTORIAL, expectedMessage);
    }



}
