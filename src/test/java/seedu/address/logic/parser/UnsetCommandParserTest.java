package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.UnsetCommand;

public class UnsetCommandParserTest {

    private UnsetCommandParser parser = new UnsetCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, "p/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE));

        // no field specified
        assertParseFailure(parser, "1", UnsetCommand.MESSAGE_NOT_UNSET);

        // no index and no field specified
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5 p/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE));

        // zero index
        assertParseFailure(parser, "0 p/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE));

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE));

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nameFieldSpecified_failure() {
        assertParseFailure(parser, "1 n/", "Name cannot be unset.");
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // phone
        String userInput = targetIndex.getOneBased() + " p/";
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setPhone(Optional.empty());
        UnsetCommand expectedCommand = new UnsetCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + " e/";
        descriptor = new EditPersonDescriptor();
        descriptor.setEmail(Optional.empty());
        expectedCommand = new UnsetCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + " a/";
        descriptor = new EditPersonDescriptor();
        descriptor.setAddress(Optional.empty());
        expectedCommand = new UnsetCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // telegram
        userInput = targetIndex.getOneBased() + " tg/";
        descriptor = new EditPersonDescriptor();
        descriptor.setTelegram(Optional.empty());
        expectedCommand = new UnsetCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + " t/";
        descriptor = new EditPersonDescriptor();
        descriptor.setTags(Collections.emptySet());
        expectedCommand = new UnsetCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " p/ e/ tg/";
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setPhone(Optional.empty());
        descriptor.setEmail(Optional.empty());
        descriptor.setTelegram(Optional.empty());
        UnsetCommand expectedCommand = new UnsetCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
