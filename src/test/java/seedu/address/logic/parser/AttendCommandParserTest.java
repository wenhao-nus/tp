package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AttendCommand;

public class AttendCommandParserTest {

    private AttendCommandParser parser = new AttendCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AttendCommand expectedCommand = new AttendCommand(INDEX_FIRST_PERSON, "CS2103T", 1);

        assertParseSuccess(parser, " 1 " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1", expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendCommand.MESSAGE_USAGE);

        // missing course prefix
        assertParseFailure(parser, " 1 " + PREFIX_WEEK + "1", expectedMessage);

        // missing week prefix
        assertParseFailure(parser, " 1 " + PREFIX_COURSE + "CS2103T", expectedMessage);

        // missing index
        assertParseFailure(parser, " " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1", expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid index
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " a " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1", expectedMessage);
        assertParseFailure(parser, " 0 " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1", expectedMessage);
        assertParseFailure(parser, " -1 " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1", expectedMessage);

        // invalid week
        assertParseFailure(parser, " 1 " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK
                + "0", "Week must be between 1 and 13");
        assertParseFailure(parser, " 1 " + PREFIX_COURSE + "CS2103T "
                + PREFIX_WEEK + "14", "Week must be between 1 and 13");
        assertParseFailure(parser, " 1 " + PREFIX_COURSE + "CS2103T "
                + PREFIX_WEEK + "abc", "Week must be a number.");
    }
}
