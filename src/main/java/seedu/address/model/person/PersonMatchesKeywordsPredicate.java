package seedu.address.model.person;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches all the categories given.
 * <p>
 * Within each of the categories
 * (represented by fields like nameKeywords, emailKeywords, tutorialCodeKeywords, courseCodeKeywords, tagNameKeywords)
 * Only one keyword need to be matched.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;

    private final List<String> addressKeywords;
    private final List<String> tagNameKeywords;
    private final List<String> telegramKeywords;
    private final List<String> tutorialCodeKeywords;
    private final List<String> courseCodeKeywords;

    /**
     * Pass in {@code null} or an empty list for the fields not specified,
     * these fields will not be filtered.
     */
    public PersonMatchesKeywordsPredicate(
            List<String> nameKeywords,
            List<String> phoneKeywords,
            List<String> emailKeywords,
            List<String> addressKeywords,
            List<String> tagNameKeywords,
            List<String> telegramKeywords,
            List<String> tutorialCodeKeywords,
            List<String> courseCodeKeywords
    ) {
        // Defensive programming
        this.nameKeywords = nameKeywords == null ? Collections.emptyList() : nameKeywords;
        this.phoneKeywords = phoneKeywords == null ? Collections.emptyList() : phoneKeywords;
        this.emailKeywords = emailKeywords == null ? Collections.emptyList() : emailKeywords;

        this.addressKeywords = addressKeywords == null ? Collections.emptyList() : addressKeywords;
        this.tagNameKeywords = tagNameKeywords == null ? Collections.emptyList() : tagNameKeywords;
        this.telegramKeywords = telegramKeywords == null ? Collections.emptyList() : telegramKeywords;
        this.tutorialCodeKeywords = tutorialCodeKeywords == null ? Collections.emptyList() : tutorialCodeKeywords;
        this.courseCodeKeywords = courseCodeKeywords == null ? Collections.emptyList() : courseCodeKeywords;
    }

    @Override
    public boolean test(Person person) {

        boolean nameMatches = nameKeywords.isEmpty() || nameKeywords.stream().anyMatch(person::nameMatches);
        boolean phoneMatches = phoneKeywords.isEmpty() || phoneKeywords.stream().anyMatch(person::phoneMatches);
        boolean emailMatches = emailKeywords.isEmpty() || emailKeywords.stream().anyMatch(person::emailMatches);

        boolean addressMatches = addressKeywords.isEmpty() || addressKeywords.stream().anyMatch(person::addressMatches);
        boolean tagMatches = tagNameKeywords.isEmpty() || tagNameKeywords.stream().anyMatch(person::tagMatches);
        boolean telegramMatches = telegramKeywords.isEmpty()
                || telegramKeywords.stream().anyMatch(person::telegramMatches);
        boolean tutorialMatches = tutorialCodeKeywords.isEmpty()
                || tutorialCodeKeywords.stream().anyMatch(person::tutorialMatches);
        boolean courseMatches = courseCodeKeywords.isEmpty()
                || courseCodeKeywords.stream().anyMatch(person::courseMatches);

        return nameMatches && phoneMatches && emailMatches
                && addressMatches && tagMatches && telegramMatches && tutorialMatches && courseMatches;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonMatchesKeywordsPredicate)) {
            return false;
        }

        PersonMatchesKeywordsPredicate otherPredicate = (PersonMatchesKeywordsPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && phoneKeywords.equals(otherPredicate.phoneKeywords)
                && emailKeywords.equals(otherPredicate.emailKeywords)
                && addressKeywords.equals(otherPredicate.addressKeywords)
                && tagNameKeywords.equals(otherPredicate.tagNameKeywords)
                && telegramKeywords.equals(otherPredicate.telegramKeywords)
                && tutorialCodeKeywords.equals(otherPredicate.tutorialCodeKeywords)
                && courseCodeKeywords.equals(otherPredicate.courseCodeKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("emailKeywords", emailKeywords)
                .add("addressKeywords", addressKeywords)
                .add("tagNameKeywords", tagNameKeywords)
                .add("telegramKeywords", telegramKeywords)
                .add("tutorialCodeKeywords", tutorialCodeKeywords)
                .add("courseCodeKeywords", courseCodeKeywords)
                .toString();
    }
}
