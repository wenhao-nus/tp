package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;


/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Telegram telegram;
    private final List<TutInfo> tutInfos = new ArrayList<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Telegram telegram, Set<Tag> tags,
                  List<TutInfo> tutInfos) {
        requireAllNonNull(name, phone, email, address, telegram, tags, tutInfos);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.telegram = telegram;
        this.tags.addAll(tags);
        this.tutInfos.addAll(tutInfos);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable tutInfo list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<TutInfo> getTutInfos() {
        return Collections.unmodifiableList(tutInfos);
    }

    public ObservableList<TutInfo> getObservableTutInfos() {
        return FXCollections.observableList(this.getTutInfos());
    }

    /**
     * Returns true if both persons have the same email.
     * This defines a weaker notion of equality between two persons.
     * The placeholder email "-" is not considered unique.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }

        if (getEmail().value.equals("-") || otherPerson.getEmail().value.equals("-")) {
            return false;
        }

        return otherPerson.getEmail().equals(getEmail());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && otherPerson.getTelegram().equals(getTelegram())
                && tags.equals(otherPerson.tags)
                && tutInfos.equals(otherPerson.tutInfos);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, telegram, tags, tutInfos);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("telegram", telegram)
                .add("tags", tags)
                .add("tutInfos", tutInfos)
                .toString();
    }

    /**
     * Returns true if the Person's name matches the keyword
     * Case-insensitive
     */
    public boolean nameMatches(String keyword) {
        return seedu.address.commons.util.StringUtil.containsIgnoreCase(name.fullName, keyword);
    }

    /**
     * Returns true if the Person's phone matches the keyword
     * Case-insensitive
     */
    public boolean phoneMatches(String keyword) {
        return seedu.address.commons.util.StringUtil.containsIgnoreCase(phone.value, keyword);
    }

    /**
     * Returns true if the Person's email matches the keyword
     * Case-insensitive
     */
    public boolean emailMatches(String keyword) {
        return seedu.address.commons.util.StringUtil.containsIgnoreCase(email.value, keyword);
    }

    /**
     * Returns true if the Person's address matches the keyword
     * Case-insensitive
     */
    public boolean addressMatches(String keyword) {
        return seedu.address.commons.util.StringUtil.containsIgnoreCase(address.value, keyword);
    }

    /**
     * Returns true if the Person's telegram handle matches the keyword
     * Case-insensitive
     */
    public boolean telegramMatches(String keyword) {
        String cleanKeyword = keyword;
        if (cleanKeyword.startsWith("@")) {
            cleanKeyword = cleanKeyword.substring(1);
        }
        return seedu.address.commons.util.StringUtil.containsIgnoreCase(telegram.value, cleanKeyword);
    }

    /**
     * Returns true if any of the Person's tags matches the keyword
     * Case-insensitive
     */
    public boolean tagMatches(String keyword) {
        return tags.stream()
                .anyMatch(tag -> tag.tagName.equalsIgnoreCase(keyword));
    }

    /**
     * Returns true if any of the Person's enrolled course codes in TutInfo matches the keyword
     * Case-insensitive
     */
    public boolean courseMatches(String keyword) {
        return tutInfos.stream()
                .anyMatch(tutInfo -> tutInfo.getCourseCode().equalsIgnoreCase(keyword));
    }

    /**
     * Returns true if any of the Person's tutorial codes in TutInfo matches the keyword
     * Case-insensitive
     */
    public boolean tutorialMatches(String keyword) {
        return tutInfos.stream()
                .anyMatch(tutInfo -> tutInfo.getTutorialCode().equalsIgnoreCase(keyword));
    }
}
