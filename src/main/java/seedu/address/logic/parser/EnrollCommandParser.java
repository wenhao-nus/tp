package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EnrollCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TutInfo;

/**
 * Parses input arguments and creates a new EnrollCommand object
 */
public class EnrollCommandParser implements Parser<EnrollCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EnrollCommand
     * and returns an EnrollCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EnrollCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSE, PREFIX_TUTORIAL);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_INVALID_INDEX + "\n" + EnrollCommand.MESSAGE_USAGE), pe);
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_COURSE, PREFIX_TUTORIAL)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EnrollCommand.MESSAGE_USAGE));
        }

        String course = ParserUtil.parseCourse(argMultimap.getValue(PREFIX_COURSE).get());
        String tutorial = ParserUtil.parseTutorial(argMultimap.getValue(PREFIX_TUTORIAL).get());

        TutInfo tutInfo = new TutInfo(course, tutorial);
        return new EnrollCommand(index, tutInfo);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
