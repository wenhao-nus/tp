package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_PREAMBLE_NOT_EMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {

    private static final Set<Prefix> VALID_PREFIXES = Set.of(
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TELEGRAM, PREFIX_TAG
    );

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand.
     * Returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, VALID_PREFIXES.toArray(Prefix[]::new));

        // Checks if there is extra text before first valid prefix.
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_PREAMBLE_NOT_EMPTY, AddCommand.MESSAGE_USAGE));
        }

        // Checks if any duplicate valid prefixes are present.
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_TELEGRAM);

        // Checks for any missing mandatory fields.
        checkMissingMandatoryFields(argMultimap);

        Person person = createPersonToAdd(argMultimap);
        return new AddCommand(person);
    }

    /**
     * Checks if any mandatory fields for {@code AddCommand}, i.e. {@code Name} and {@code Email} are missing.
     */
    private void checkMissingMandatoryFields(ArgumentMultimap argMultimap) throws ParseException {
        List<String> missingFields = new ArrayList<>();

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            missingFields.add(AddCommand.MESSAGE_MISSING_NAME);
        }
        if (!arePrefixesPresent(argMultimap, PREFIX_EMAIL)) {
            missingFields.add(AddCommand.MESSAGE_MISSING_EMAIL);
        }

        if (!(missingFields.isEmpty())) {
            String missingFieldsString = AddCommand.MESSAGE_MISSING_PARAMS
                    + String.join(" and ", missingFields)
                    + "\n" + AddCommand.MESSAGE_USAGE;
            throw new ParseException(missingFieldsString);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses all fields from {@code ArgumentMultimap} and creates a {@code Person} to be added.
     */
    private Person createPersonToAdd(ArgumentMultimap argMultimap) throws ParseException {
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());

        Optional<Phone> phone = (argMultimap.getValue(PREFIX_PHONE).isPresent())
                ? Optional.of(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()))
                : Optional.empty();

        Optional<Address> address = (argMultimap.getValue(PREFIX_ADDRESS).isPresent())
                ? Optional.of(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()))
                : Optional.empty();

        Optional<Telegram> telegram = (argMultimap.getValue(PREFIX_TELEGRAM).isPresent())
                ? Optional.of(ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get()))
                : Optional.empty();

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new Person(name, phone, email, address, telegram, tagList, new ArrayList<>());
    }

}
