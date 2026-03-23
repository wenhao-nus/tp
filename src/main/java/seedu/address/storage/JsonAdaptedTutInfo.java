package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.TutInfo;

/**
 * Jackson-friendly version of {@link TutInfo}.
 */
class JsonAdaptedTutInfo {

    private final String courseCode;
    private final String tutorialCode;
    private final List<Boolean> attendance = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedTutInfo} with the given parameters.
     */
    @JsonCreator
    public JsonAdaptedTutInfo(@JsonProperty("courseCode") String courseCode,
                              @JsonProperty("tutorialCode") String tutorialCode,
                              @JsonProperty("attendance") List<Boolean> attendance) {
        this.courseCode = courseCode;
        this.tutorialCode = tutorialCode;
        if (attendance != null) {
            this.attendance.addAll(attendance);
        }
    }

    /**
     * Converts a given {@code TutInfo} into this class for Jackson use.
     */
    public JsonAdaptedTutInfo(TutInfo source) {
        courseCode = source.getCourseCode();
        tutorialCode = source.getTutorialCode();

        // Transform the boolean array into an arrayList
        for (boolean attended : source.getAttendance()) {
            attendance.add(attended);
        }
    }

    /**
     * Converts this Jackson-friendly adapted TutInfo object into the model's {@code TutInfo} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted TutInfo.
     */
    public TutInfo toModelType() throws IllegalValueException {
        if (courseCode == null || tutorialCode == null || attendance == null) {
            throw new IllegalValueException("TutInfo field(s) missing!");
        }
        if (!TutInfo.isValidCode(courseCode)) {
            throw new IllegalValueException(TutInfo.MESSAGE_CONSTRAINTS);
        }
        if (!TutInfo.isValidCode(tutorialCode)) {
            throw new IllegalValueException(TutInfo.MESSAGE_CONSTRAINTS);
        }
        if (attendance.size() != TutInfo.NUMBER_OF_WEEKS_PER_SEM) {
            throw new IllegalValueException("Attendance must have " + TutInfo.NUMBER_OF_WEEKS_PER_SEM + " weeks!");
        }

        boolean[] modelAttendance = new boolean[TutInfo.NUMBER_OF_WEEKS_PER_SEM];
        for (int i = 0; i < TutInfo.NUMBER_OF_WEEKS_PER_SEM; i++) {
            modelAttendance[i] = attendance.get(i);
        }

        return new TutInfo(courseCode, tutorialCode, modelAttendance);
    }

}
