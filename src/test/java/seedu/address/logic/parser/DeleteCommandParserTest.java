package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.InvalidIndexMessages;
import seedu.address.logic.commands.DeleteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommandParser code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommandParser.
 *
 * Therefore, only one representative invalid input is tested here. Path variation of all specific
 * invalid indices (empty string, multiple indices, non-integer, non-numeric, zero, negative,
 * overflow indices) occur inside ParserUtil, and are tested in {@link ParserUtilTest}.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String exceptedMessages = String.format(DeleteCommand.MESSAGE_DELETE_INDEX_ERROR
                + InvalidIndexMessages.MESSAGE_INDEX_NON_NUMERIC + "\n%s",
                DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "a", exceptedMessages);
    }
}
