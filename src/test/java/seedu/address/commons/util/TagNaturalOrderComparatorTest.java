package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class TagNaturalOrderComparatorTest {
    private final TagNaturalOrderComparator comparator = new TagNaturalOrderComparator();

    @Test
    public void compare_alphabeticalOrder() {
        Tag tagA = new Tag("app");
        Tag tagB = new Tag("ben");
        Tag tagC = new Tag("Car");

        assertTrue(comparator.compare(tagA, tagB) < 0);
        assertTrue(comparator.compare(tagA, tagC) < 0); // case-insensitive: app before Car
    }

    @Test
    public void compare_numericalOrder() {
        Tag tag2 = new Tag("tag2");
        Tag tag11 = new Tag("tag11");
        assertTrue(comparator.compare(tag2, tag11) < 0); // numerical comparison of 2 < 11
    }

    @Test
    public void compare_leadingZeros() {
        Tag tag005 = new Tag("tag005");
        Tag tag05 = new Tag("tag05");
        Tag tag5 = new Tag("tag5");

        // For same numerical values, number with more leading zeros would be put first
        assertTrue(comparator.compare(tag005, tag05) < 0);
        assertTrue(comparator.compare(tag05, tag5) < 0);
    }

    @Test
    public void compare_overflowNumbers() {
        Tag tagOverFlow1 = new Tag("11111111232222344455");
        Tag tagOverFlow2 = new Tag("11111111232222344455");
        Tag tagOverFlow3 = new Tag("01111111123222234445");
        assertTrue(comparator.compare(tagOverFlow1, tagOverFlow2) == 0);
        assertTrue(comparator.compare(tagOverFlow3, tagOverFlow2) < 0);
    }

    @Test
    public void compare_remainingLength() {
        Tag shortTag = new Tag("tag");
        Tag longTag = new Tag("tagLong");
        Tag sameShortTag = new Tag("tag");

        // For tags with same prefix, shorter tags would come first
        assertTrue(comparator.compare(shortTag, longTag) < 0);
        assertTrue(comparator.compare(longTag, shortTag) > 0);
        assertTrue(comparator.compare(sameShortTag, shortTag) == 0);
    }

    @Test
    public void compare_alphanumericCombined() {
        Tag tag1 = new Tag("tag95text");
        Tag tag2 = new Tag("tag95text");
        Tag tag3 = new Tag("tag95line");

        // Ensure alphabetical order is preserved after numerical ordering
        assertTrue(comparator.compare(tag1, tag2) == 0);
        assertTrue(comparator.compare(tag1, tag3) > 0);
    }
}
