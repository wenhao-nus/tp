package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.person.TutInfo;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_NAME + "NAME "
            + PREFIX_EMAIL + "EMAIL " + "[" + PREFIX_PHONE + "PHONE] " + "["
            + PREFIX_ADDRESS + "ADDRESS] " + "[" + PREFIX_TELEGRAM + "TELEGRAM] "
            + "[" + PREFIX_TAG + "TAG]...\n" + "Example: "
            + COMMAND_WORD + " 1 " + PREFIX_EMAIL + "johndoe@example.com "
            + PREFIX_PHONE + "91234567" + PREFIX_TELEGRAM + "johndoe_new";

    public static final String MESSAGE_INDEX_AND_PREFIX_MISSING =
                "An index and at least one field prefix (e.g., n/, tg/) with a valid value "
                + "must be provided to edit a person.";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_EDIT_NO_CHANGES =
            "Note: The changes you entered are the same as the current information of the person below. "
            + "So, nothing has been updated.\n%s";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "Email, Telegram handle, and phone number must be unique!\n"
            + " A contact with the same email, phone number, or Telegram handle exists in the addressbook.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Handles case of empty current displayed list
        if (lastShownList.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_EMPTY_DISPLAYED_LIST, MESSAGE_USAGE));
        }

        if (isIndexOutOfBounds(lastShownList)) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_INDEX_OUT_OF_BOUNDS + "\n%s", MESSAGE_USAGE));
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        try {
            model.setPerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.setPersonToShow(editedPerson); // Always show the edited person

        if (editedPerson != null && personToEdit.equals(editedPerson)) {
            return new CommandResult(String.format(MESSAGE_EDIT_NO_CHANGES, Messages.format(editedPerson)));
        }

        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson))
                + Messages.MESSAGE_TAG_NOTE);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());

        Optional<Phone> updatedPhone = editPersonDescriptor.getPhone().or(() -> personToEdit.getPhone());
        Optional<Address> updatedAddress = editPersonDescriptor.getAddress().or(() -> personToEdit.getAddress());
        Optional<Telegram> updatedTelegram = editPersonDescriptor.getTelegram().or(() -> personToEdit.getTelegram());

        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        List<TutInfo> tutInfos = personToEdit.getTutInfos();

        return new Person(
                updatedName,
                updatedPhone,
                updatedEmail,
                updatedAddress,
                updatedTelegram,
                updatedTags,
                tutInfos
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
    * Returns true if the target index is outside the range of the displayed person list.
    */
    private boolean isIndexOutOfBounds(List<Person> list) {
        return index.getZeroBased() >= list.size();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Telegram telegram;
        private Set<Tag> tags;
        private List<TutInfo> tutInfos;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} and {@code tutInfos} are used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTelegram(toCopy.telegram);
            setTutInfos(toCopy.tutInfos);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, telegram, tags, tutInfos);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setTelegram(Telegram telegram) {
            this.telegram = telegram;
        }

        public Optional<Telegram> getTelegram() {
            return Optional.ofNullable(telegram);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code tutInfos} to this object's {@code tutInfos}.
         * A defensive copy of {@code tutInfos} is used internally.
         */
        public void setTutInfos(List<TutInfo> tutInfos) {
            this.tutInfos = (tutInfos != null) ? new ArrayList<>(tutInfos) : null;
        }

        /**
         * Returns an unmodifiable list of tut infos, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tutInfos} is null.
         */
        public Optional<List<TutInfo>> getTutInfos() {
            return (tutInfos != null) ? Optional.of(Collections.unmodifiableList(tutInfos)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(telegram, otherEditPersonDescriptor.telegram)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(tutInfos, otherEditPersonDescriptor.tutInfos);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("telegram", telegram)
                    .add("address", address)
                    .add("tutInfos", tutInfos)
                    .add("tags", tags)
                    .toString();
        }
    }
}
