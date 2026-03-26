package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutInfo;

/**
 * Marks a person's attendance for a week in a course as true.
 */
public class AttendCommand extends Command {

    public static final String COMMAND_WORD = "attend";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks attendance for a person.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_COURSE + "COURSE_CODE "
            + PREFIX_WEEK + "WEEK\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1";

    public static final String MESSAGE_SUCCESS = "Marked attendance for student at index %1$s: %2$s attended "
            + "%3$s %4$s Week %5$s's tutorial";
    public static final String MESSAGE_COURSE_NOT_FOUND = "This person is not enrolled in %1$s!";

    private final Index index;
    private final String courseCode;
    private final int week;

    /**
     * Creates an AttendCommand to mark the person's attendance at {@code index} for {@code courseCode}
     * on {@code week}.
     */
    public AttendCommand(Index index, String courseCode, int week) {
        requireNonNull(index);
        requireNonNull(courseCode);
        this.index = index;
        this.courseCode = courseCode;
        this.week = week;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        List<TutInfo> tutInfos = personToEdit.getTutInfos();

        TutInfo tutInfoToUpdate = null;
        int tutInfoIndex = -1;
        for (int i = 0; i < tutInfos.size(); i++) {
            if (tutInfos.get(i).getCourseCode().equals(courseCode)) {
                tutInfoToUpdate = tutInfos.get(i);
                tutInfoIndex = i;
                break;
            }
        }

        if (tutInfoToUpdate == null) {
            throw new CommandException(String.format(MESSAGE_COURSE_NOT_FOUND, courseCode));
        }

        TutInfo updatedTutInfo = tutInfoToUpdate.setAttendance(week, true);

        List<TutInfo> updatedTutInfos = new ArrayList<>(tutInfos);
        updatedTutInfos.set(tutInfoIndex, updatedTutInfo);

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTelegram(), personToEdit.getTags(),
                updatedTutInfos
        );

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.setPersonToShow(editedPerson); // Always show the attended person

        return new CommandResult(String.format(MESSAGE_SUCCESS, index.getOneBased(), personToEdit.getName(),
                courseCode, tutInfoToUpdate.getTutorialCode(), week));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AttendCommand)) {
            return false;
        }
        AttendCommand e = (AttendCommand) other;
        return index.equals(e.index) && courseCode.equals(e.courseCode) && week == e.week;
    }
}
