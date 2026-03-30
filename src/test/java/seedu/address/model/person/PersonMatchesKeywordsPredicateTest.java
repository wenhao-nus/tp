package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        // Order: name, phone, email, address, tag, telegram, tutorial, course
        PersonMatchesKeywordsPredicate firstPredicate = new PersonMatchesKeywordsPredicate(
                firstPredicateKeywordList, null, null, null, null, null, null, null);
        PersonMatchesKeywordsPredicate secondPredicate = new PersonMatchesKeywordsPredicate(
                secondPredicateKeywordList, null, null, null, null, null, null, null);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonMatchesKeywordsPredicate firstPredicateCopy = new PersonMatchesKeywordsPredicate(
                firstPredicateKeywordList, null, null, null, null, null, null, null);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_fieldsMatch_returnsTrue() {
        TutInfo cs2103Tut = new TutInfo("CS2103T", "G01");

        // One field (name)
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(
                Collections.singletonList("Ali"), null, null, null, null, null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords in one field
        predicate = new PersonMatchesKeywordsPredicate(
                Arrays.asList("Alice", "Bob"), null, null, null, null, null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Bob Carol").build()));

        // Multiple fields
        predicate = new PersonMatchesKeywordsPredicate(
                Collections.singletonList("Alice"), null, Collections.singletonList("alice@example.com"),
                null, null, null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@example.com").build()));

        // Phone matching
        predicate = new PersonMatchesKeywordsPredicate(
                null, Collections.singletonList("9435"), null, null, null, null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Address matching
        predicate = new PersonMatchesKeywordsPredicate(
                null, null, null, Collections.singletonList("Jurong"), null, null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withAddress("123, Jurong West").build()));

        // Telegram matching
        predicate = new PersonMatchesKeywordsPredicate(
                null, null, null, null, null, Collections.singletonList("alice_tele"), null, null);
        assertTrue(predicate.test(new PersonBuilder().withTelegram("alice_tele").build()));

        // Tutorial Code matching
        predicate = new PersonMatchesKeywordsPredicate(
                null, null, null, null, null, null, Collections.singletonList("G01"), null);
        assertTrue(predicate.test(new PersonBuilder().withTutInfos(Arrays.asList(cs2103Tut))
                .build()));

        // Course Code matching
        predicate = new PersonMatchesKeywordsPredicate(
                null, null, null, null, null, null, null, Collections.singletonList("CS2103T"));
        assertTrue(predicate.test(new PersonBuilder().withTutInfos(Arrays.asList(cs2103Tut))
                .build()));

        // Tag matching
        predicate = new PersonMatchesKeywordsPredicate(
                null, null, null, null, Collections.singletonList("friends"), null, null, null);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));
    }

    @Test
    public void test_fieldsDoNotMatch_returnsFalse() {
        // Non-matching keyword
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(
                Collections.singletonList("Bob"), null, null, null, null, null, null, null);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // One field matches, another does not
        predicate = new PersonMatchesKeywordsPredicate(
                Collections.singletonList("Alice"), null, Collections.singletonList("bob@example.com"),
                null, null, null, null, null);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@example.com").build()));
    }

    @Test
    public void string() {
        List<String> names = List.of("name1", "name2");
        List<String> emails = List.of("email1");
        // Order: name, phone, email, address, tag, telegram, tutorial, course
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(
                names, null, emails, null, null, null, null, null);

        String expected = PersonMatchesKeywordsPredicate.class.getCanonicalName() + "{"
                + "nameKeywords=" + names + ", "
                + "phoneKeywords=" + Collections.emptyList() + ", "
                + "emailKeywords=" + emails + ", "
                + "addressKeywords=" + Collections.emptyList() + ", "
                + "tagNameKeywords=" + Collections.emptyList() + ", "
                + "telegramKeywords=" + Collections.emptyList() + ", "
                + "tutorialCodeKeywords=" + Collections.emptyList() + ", "
                + "courseCodeKeywords=" + Collections.emptyList() + "}";
        assertEquals(expected, predicate.toString());
    }
}
