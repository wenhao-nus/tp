package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnenrollCommand;

public class UnenrollCommandParserTest {

    private UnenrollCommandParser parser = new UnenrollCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String expectedCourseCode = "CS2103T";
        UnenrollCommand expectedCommand = new UnenrollCommand(INDEX_FIRST_PERSON, expectedCourseCode);

        assertParseSuccess(parser, " 1 c/CS2103T", expectedCommand);

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnenrollCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " 1", expectedMessage);

        assertParseFailure(parser, " c/CS2103T", expectedMessage);
    }
}
