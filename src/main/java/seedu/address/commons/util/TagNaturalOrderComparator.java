package seedu.address.commons.util;

import java.util.Comparator;

import seedu.address.model.tag.Tag;

/**
 * Comparator for tags (that accepts alphanumeric names only) in natural order.
 * Compares alphabets case-insensitively, and number parts are compared as the natural order of numbers.
 * If two numbers are equal, the one with more leading zeros will be first.
 * Example of tags ordering: atag, tag02, tag2, Tag3, tag11, tag33.
 */
public class TagNaturalOrderComparator implements Comparator<Tag> {

    @Override
    public int compare(Tag t1, Tag t2) {
        return compareTagNames(t1.getTagName(), t2.getTagName());
    }

    /**
     * Compares two tag names alpha-numerically with natural order.
     *
     * @return A negative integer, zero, or positive integer if the first tag is less than,
     *         equal to, or greater than the second tag in natural ordering respectively.
     */
    private int compareTagNames(String tag1, String tag2) {
        int index1 = 0;
        int index2 = 0;
        int firstTagLength = tag1.length();
        int secondTagLength = tag2.length();

        while (index1 < firstTagLength && index2 < secondTagLength) {
            char c1 = tag1.charAt(index1);
            char c2 = tag2.charAt(index2);

            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                int comparisonResult = compareNumericPart(tag1, index1, tag2, index2);
                if (comparisonResult != 0) {
                    return comparisonResult;
                }

                index1 += extractNumber(tag1, index1).length();
                index2 += extractNumber(tag2, index2).length();
            } else {
                int comparisonResult = Character.toLowerCase(c1) - Character.toLowerCase(c2);
                if (comparisonResult != 0) {
                    return comparisonResult;
                }

                index1++;
                index2++;
            }
        }

        return compareRemainingLengths(index1, firstTagLength, index2, secondTagLength);
    }

    /**
     * Compares numeric parts as numbers with more leading-zero coming first.
     */
    private int compareNumericPart(String s1, int startIndex1, String s2, int startIndex2) {
        String num1 = extractNumber(s1, startIndex1);
        String num2 = extractNumber(s2, startIndex2);

        int int1 = Integer.parseInt(num1);
        int int2 = Integer.parseInt(num2);

        if (int1 != int2) {
            return Integer.compare(int1, int2);
        }

        return Integer.compare(num2.length(), num1.length());
    }

    /**
     * Compares remaining tags lengths, where tag with remaining length will be placed after.
     */
    private int compareRemainingLengths(int i, int length1, int j, int length2) {
        if (i < length1) {
            return 1;
        } else if (j < length2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Extracts consecutive digits starting at a given index as a number.
     */
    private String extractNumber(String s, int index) {
        int start = index;
        while (index < s.length() && Character.isDigit(s.charAt(index))) {
            index++;
        }
        return s.substring(start, index);
    }
}
