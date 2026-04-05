package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EnrollCommand;
import seedu.address.model.person.TutInfo;

public class EnrollCommandParserTest {

    private EnrollCommandParser parser = new EnrollCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        TutInfo expectedTutInfo = new TutInfo("CS2103T", "T01");
        EnrollCommand expectedCommand = new EnrollCommand(INDEX_FIRST_PERSON, expectedTutInfo);

        assertParseSuccess(parser, " 1 c/CS2103T tut/T01", expectedCommand);

        assertParseSuccess(parser, " 1 c/cs2103T tut/t01", expectedCommand);

        assertParseSuccess(parser, " 1 tut/T01 c/CS2103T", expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedInvalidIndexMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                Messages.MESSAGE_INVALID_INDEX + "\n" + EnrollCommand.MESSAGE_USAGE);

        String expectedGeneralMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EnrollCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " 1 c/CS2103T", expectedGeneralMessage);

        assertParseFailure(parser, " 1 tut/T01", expectedGeneralMessage);

        assertParseFailure(parser, " c/CS2103T tut/T01", expectedInvalidIndexMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, " 1 c/CS2103T! tut/T01", TutInfo.MESSAGE_CONSTRAINTS);

        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT,
                Messages.MESSAGE_INVALID_INDEX + "\n" + EnrollCommand.MESSAGE_USAGE
        );

        assertParseFailure(parser, " a c/CS2103T tut/T01", expectedMessage);
        assertParseFailure(parser, " 0 c/CS2103T tut/T01", expectedMessage);
        assertParseFailure(parser, " -1 c/CS2103T tut/T01", expectedMessage);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COURSE);
        assertParseFailure(parser, " 1 " + PREFIX_COURSE + "CS2103T " + PREFIX_COURSE + "CS2101 "
                + PREFIX_TUTORIAL + "T01", expectedMessage);

        expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TUTORIAL);
        assertParseFailure(parser, " 1 " + PREFIX_COURSE + "CS2103T " + PREFIX_TUTORIAL + "T01 "
                + PREFIX_TUTORIAL + "T02", expectedMessage);
    }
}
