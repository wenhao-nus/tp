---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

---

## **Acknowledgements**

- This project is based on the [AddressBook-Level3](https://github.com/se-edu/addressbook-level3) project by the SE-EDU initiative.
- Parts of the structure and documentation style were adapted from the [SE-EDU project portfolio and documentation guides](https://se-education.org/docs/).
- Generative AI tools were used to assist with drafting and refining some test cases. All AI-assisted outputs were reviewed, edited, and validated by the team before inclusion in the codebase.

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The **_Architecture Diagram_** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-T10-4/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-T10-4/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.

- At app launch, it initialises the other components in the correct sequence, and connects them up with each other.
- At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

- [**`UI`**](#ui-component): The UI of the App.
- [**`Logic`**](#logic-component): The command executor.
- [**`Model`**](#model-component): Holds the data of the App in memory.
- [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

- defines its _API_ in an `interface` with the same name as the Component.
- implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-T10-4/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-T10-4/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-T10-4/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

- executes user commands using the `Logic` component.
- listens for changes to `Model` data so that the UI can be updated with the modified data.
- keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
- depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-T10-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:

- When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
- All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-T10-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />

The `Model` component,

- stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
- stores the currently 'filtered' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
- stores the Person object that is currently selected to be shown in the detailed view as an `ObjectProperty<Person>`. This property is observable so that the UI can automatically update when the selected person changes.
- stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
- does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-T10-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,

- can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
- inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
- depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

---

## **Documentation, logging, testing, configuration, dev-ops**

- [Documentation guide](Documentation.md)
- [Testing guide](Testing.md)
- [Logging guide](Logging.md)
- [Configuration guide](Configuration.md)
- [DevOps guide](DevOps.md)

---

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a NUS computer science teaching assistant managing multiple tutorial classes
* needs to manage students in each tutorial session efficiently
* needs to organise and track contacts across distinct academic roles (students, professors, fellow TAs)
* requires the ability to categorise and quickly retrieve contacts using specific fields (e.g., by tutorial group)
* frequently handles incomplete contact profiles that need to be updated iteratively (e.g., adding Telegram handles later)
* needs a reliable way to archive and export contact data at the end of the academic semester (e.g., a JSON file)
* prefers a streamlined, role-specific tool over a general-purpose address book
* can type fast
* prefers desktop apps over other types
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:
This product aims to streamline communication from TAs to their students, other TAs, teaching staff, and professors. It achieves this by organising contacts into courses, tutorial groups and tags. It supports custom contact categories (e.g., Telegram handles), and more searching functionality (e.g., by groups and/or by email etc.). It also makes contacts storing more flexible by only making names and email addresses mandatory. 

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                                                   | So that I can…​                                                                                    |
|----------|---------|----------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| `* * *`  | TA      | add a contact to the address book                              | keep a record of a person.                                                                         |
| `* * *`  | TA      | delete a contact from the app                                  | remove unwanted entries and keep my contact list accurate.                                         |
| `* * *`  | TA      | view all contacts in the app                                   | see all entries in the contact list.                                                               |
| `* *`    | TA      | edit details of a contact                                      | keep contact information up to date                                                                |
| `* *`    | TA      | find contacts from the app by one or more specific attributes  | see specific types of contacts(e.g.,a specific tag/course) in the contact list.                    |
| `* *`    | TA      | view detailed information of a contact                         | access all relevant information including attendance record about the student                      |
| `*`      | TA      | enroll a student to a course and a tutorial group              | distinguish my students from different courses                                                     |
| `*`      | TA      | remove a student from a tutorial or course                     | update course droppings or enrollment mistakes                                                     |
| `*`      | TA      | mark a tutorial session from a course of a student as attended | identify if a student needs help                                                                   |
| `*`      | TA      | update a student’s attendance record                           | fix any incorrect attendance records (i.e.,unattend a session)                                     |
| `*`      | TA      | assign a contact tags                                          | specify  roles for my contacts (e.g.,fellow TAs) or mark them (e.g.,mark a student as an absentee) |

### Use cases

(For all use cases below, the **System** is `TAConnect` and the **Actor** is `NUS Computer Science Teaching Assistant (NUS CS TA)` unless specified otherwise)

<ins>**Use case: UC01 - View All Contacts**</ins>

**Guarantees:** All the contacts in the address book are displayed in the contact list.

**MSS**

1. TA requests to view their list of all contacts.
2. TAConnect lists all contacts and displays a success message.

   Use case ends.

**Extensions**

- 1a. No contacts exist.
  - 1a1. TAConnect displays an empty state message.

    Use case ends.

<ins>**Use case: UC02 - Add A Contact**</ins>

**Guarantees:** A new contact is stored in the address book only if the input is valid.

**MSS**

1. TA requests to add a contact with a name and email, along with any optional details (phone number, address, Telegram handle, tags).
2. TAConnect validates the input.
3. TAConnect adds the contact, displays a success message, and shows the updated contact list with the new contact's details.

   Use case ends.

**Extensions**

- 2a. TAConnect detects invalid input.
  - 2a1. TAConnect informs TA of invalid input and displays the correct format with an example.

    Use case ends.

- 2b. TAConnect detects duplicate contact (i.e. new contact to add has one or more of the same following fields as an existing contact: email, Telegram handle, phone number)
  - 2b1. TAConnect informs TA that a contact with the same email, phone number, or Telegram handle already exists.

    Use case ends.

<ins>**Use case: UC03 - Delete A Contact**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The chosen contact is removed from the address book only if the contact index is valid.

**MSS**

1. TA requests to delete a specific contact.
2. TAConnect validates the input.
3. TAConnect deletes the contact, displays a success message, and shows the updated contact list.

   Use case ends.

**Extensions**

- 2a. TAConnect detects an invalid or out-of-range contact index.
  - 2a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 2b. TAConnect detects other invalid input.
  - 2b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

<ins>**Use case: UC04 - Edit A Contact**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The contact's details are updated only if the input and contact index are valid.

**MSS**

1. TA requests to edit specific field(s) of a specific contact with new value(s).
2. TAConnect validates the new value(s) for the specified field(s).
3. TAConnect updates the contact, displays a success message, and shows the updated contact list.

   Use case ends.

**Extensions**

- 1a. TAConnect detects an invalid or out-of-range contact index.
  - 1a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 1b. TAConnect detects other invalid input (e.g. no fields provided, invalid field values).
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.
  
- 2a. TAConnect detects duplicate contact (i.e. edited contact has one or more of the same following fields as an existing contact: email, Telegram handle, phone number)
    - 2a1. TAConnect informs TA that a contact with the same email, phone number, or Telegram handle already exists.

      Use case ends.

<ins>**Use case: UC05 - Search/Filter Contacts**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** Contacts matching the search criteria are displayed only if the input is valid.

**MSS**

1. TA enters a search query with a valid combination of filter criteria (e.g. name, course, tutorial group, tag).
2. TAConnect retrieves the relevant contacts, displays a success message, and shows the filtered contact list.

   Use case ends.

**Extensions**

- 1a. TA specifies a tutorial group without a course code.
  - 1a1. TAConnect informs TA that a course code is required when filtering by tutorial group.

    Use case ends.

- 1b. TAConnect detects other invalid input.
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

- 2a. No contacts match the given criteria.
  - 2a1. TAConnect informs TA that no matching contacts were found.

    Use case ends.

<ins>**Use case: UC06 - View A Contact**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The full details of the specified contact are displayed only if the contact index is valid.

**MSS**

1. TA requests to view the full details of a specific contact.
2. TAConnect displays the full details of the contact.

   Use case ends.

**Extensions**

- 1a. TAConnect detects an invalid or out-of-range contact index.
  - 1a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 1b. TAConnect detects other invalid input.
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

<ins>**Use case: UC07 - Enroll A Student**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The specified student is enrolled in the given course and tutorial group only if the input is valid.

**MSS**

1. TA requests to enroll a specific contact into a course and tutorial group.
2. TAConnect validates the input.
3. TAConnect enrolls the student into the course and tutorial group, displays a success message, and shows the updated contact details.

   Use case ends.

**Extensions**

- 1a. TAConnect detects an invalid or out-of-range contact index.
  - 1a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 1b. TAConnect detects other invalid input (e.g. missing course code or tutorial group).
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

- 2a. Student is already enrolled in the given course and tutorial group.
  - 2a1. TAConnect informs TA that the student is already enrolled.

    Use case ends.

<ins>**Use case: UC08 - Unenroll A Student**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The specified student is unenrolled from the given course only if the input is valid.

**MSS**

1. TA requests to unenroll a specific contact from a course.
2. TAConnect validates the input.
3. TAConnect unenrolls the student from the course, displays a success message, and shows the updated contact details.

   Use case ends.

**Extensions**

- 1a. TAConnect detects an invalid or out-of-range contact index.
  - 1a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 1b. TAConnect detects other invalid input (e.g. missing course code, or unnecessarily provided tutorial group).
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

- 2a. Student is not enrolled in the given course.
  - 2a1. TAConnect informs TA that the student is not enrolled in the given course.

    Use case ends.

<ins>**Use case: UC09 - Mark Attendance**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The specified student's attendance is marked for the given course and week only if the input is valid.

**MSS**

1. TA requests to mark attendance for a specific contact for a given course and week.
2. TAConnect validates the input.
3. TAConnect marks the attendance, displays a success message, and shows the updated contact details.

   Use case ends.

**Extensions**

- 1a. TAConnect detects an invalid or out-of-range contact index.
  - 1a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 1b. TAConnect detects other invalid input (e.g. missing course code, missing week number, week number out of range 1-13, unnecessarily provided tutorial group).
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

- 2a. Student is not enrolled in the given course.
  - 2a1. TAConnect informs TA that the student is not enrolled in the given course.

    Use case ends.

<ins>**Use case: UC10 - Unmark Attendance**</ins>

**Preconditions:** TA has at least one contact in their list.

**Guarantees:** The specified student's attendance is unmarked for the given course and week only if the input is valid.

**MSS**

1. TA requests to unmark attendance for a specific contact for a given course and week.
2. TAConnect validates the input.
3. TAConnect unmarks the attendance, displays a success message, and shows the updated contact details.

   Use case ends.

**Extensions**

- 1a. TAConnect detects an invalid or out-of-range contact index.
  - 1a1. TAConnect informs TA of the invalid index and displays the correct format with an example.

    Use case ends.

- 1b. TAConnect detects other invalid input (e.g. missing course code, missing week number, week number out of range 1-13, unnecessarily provided tutorial group).
  - 1b1. TAConnect informs TA of the invalid input and displays the correct format with an example.

    Use case ends.

- 2a. Student is not enrolled in the given course.
  - 2a1. TAConnect informs TA that the student is not enrolled in the given course.

    Use case ends.

### Non-Functional Requirements

1. The application should run on Windows, Linux, and macOS with Java `17` installed.
2. The system should respond to all user commands and update the GUI within 2 seconds.
3. The application should launch and display the GUI with loaded contact data within 5 seconds.
4. The system should support up to 250 contacts without violating performance requirements defined by response time of at most 2 seconds.
5. The software should take no more than 50MB of space.
6. The application should function without an internet connection and external servers.
7. The system should handle invalid inputs gracefully by displaying clear error messages instead of crashing.
8. The application should be usable directly via the provided single JAR file without requiring an installer.
9. All application data should be stored locally in a human-editable JSON file.
10. The system should prevent corruption of data through modifications done using the application with commands. Any corrupted files or data loss caused by manual edits of the JSON files are outside of the scope of this requirement.
11. All changes to the addressbook should be automatically saved to the JSON file immediately after commands that modifies data.
12. GUI should allow full functionality at standard screen resolution defined as 1920×1080 and higher, at 100% and 125% display scale.
13. The application should allow users with above average typing speed for regular English text (52 words per minute) to complete supported tasks using CLI quicker than using the GUI or mouse.
14. The application should be single-user based and not support concurrent multi-user access.

### Glossary

* **Teaching Assistant (TA)**: A member of the teaching team who supports a course by running tutorials/labs, facilitating discussions, answering student questions, and coordinating with professors, course admins, and other TAs.
* **Tag**: A user-defined label attached to a contact to classify and organise them (e.g., student, prof, cs2103, tut01, projectgrpA). Tags enable quick filtering and searching across the contact list.
* **Course**: A structured module offered by the university with a unique course code (e.g. CS2101).
* **Tutorial/Lab Group**: A specific class grouping of students with a tutorial/lab code (e.g. Tut10, Lab11) of a course assigned to a TA.
* **Student**: A NUS Computer Science enrolled in a course supported by the TA.

---

## **Appendix: Planned Enhancements**

Team size: `5`

1. **Support non-ASCII characters in person names:** Currently, the application rejects names containing non-ASCII characters, preventing users from entering valid international names such as `José García` and `Müller` which limits usability for users. After this enhancement, commands such as `add n/José García e/jose@nus.edu.sg` will be accepted, and the name will be correctly stored and displayed across the application.

2. **Support international phone number formats:** Currently, the application only accepts phone numbers that consist of digits only and are at least three digits long. This limits valid real-world formats such as international phone numbers with a leading “+” for country codes. We plan to update the validation rules to allow phone numbers with an optional leading “+” while maintaining a valid length restriction. After this enhancement, users will be able to enter internationally valid phone numbers such as `+6512345678`, and have them correctly stored and displayed across the application.

3. **Add swap command for swapping a student’s tutorial group directly:** Currently, changing a student’s tutorial group requires unenrolling the student from the course and enrolling them again with a new tutorial group, which is inconvenient. We plan to introduce a `swap` command to allow direct updating of a student’s tutorial group within an existing course enrollment. After this enhancement, users will be able to swap a student’s tutorial group in a single step to improve usability.

4. **Support multiple tutorial groups per course:** Currently, each course can only be associated with a single tutorial group per student. This is restrictive for modules that have multiple types of classes such as tutorials, labs, or recitations. We plan to extend the system to allow multiple lesson groups to be associated with a single course for each student. After this enhancement, users will be able to record and manage different lesson groups (e.g. tutorials, labs, recitations) under the same course for each student.

5. **Improve flexibility of phone number input validation:** Currently, the application only accepts phone numbers that consist strictly of digits. This can be overly restrictive, as users may naturally enter phone numbers with spaces or additional non-numeric labels for readability. For example, a user might input `1234 5678 (HP) 1111 3333 (Home)`. We plan to relax the validation rules to allow such flexible input formats, including non-digit characters used for formatting to make the application more user-friendly for contact entry.

6. **Improve information visibility for attendance card:** Currently, when the window width is narrow or when course-related information is long, the attendance card in the full details viewing section may not display all content (such as course code, tutorial group, or week information). This results in truncated display due to limited horizontal space. We plan to improve this by ensuring the attendance card layout adapts to available horizontal space so that all fields remain fully visible without being cut off. This will ensure that course, tutorial, and week information can always be fully viewed within the card regardless of window size.

7. **Improve error message for missing spaces before prefixes:** Currently, when users omit spaces before command prefixes, the input may be incorrectly parsed as a single value, leading to misleading error messages. For example, in `add n/John Doe e/johndoe@example.com p/12345678tg/@johnDoe`, the system will treat `12345678tg/@johnDoe` as a single phone number instead of detecting the missing space before the `tg/` prefix. We plan to provide a specific error message indicating the formatting issue (e.g. missing space before the next prefix).<br>At the same time, the parsing logic must avoid incorrectly treating prefix-like patterns within valid field inputs (e.g. `a/Blk 123B #12-34n/2`) as actual command prefixes, as these are part of valid user input rather than command structure. This ensures correct detection of invalid prefix formatting while preserving flexibility in free-text fields instead of blocking them overzealously.

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch
   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

### Adding a person

1. Adding a new person with all fields
   1. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 tg/@johndoe t/student`<br>
      Expected: A new contact with the specified details is added to the list. Details of the newly added contact shown in the status message.

2. Adding a new person with only mandatory fields
   1. Test case: `add n/Alex Yeoh e/alexyeoh@example.com`<br>
      Expected: A new contact with the name `Alex Yeoh` and email of `alexyeoh@example.com` is added to the list. Details of the newly added contact shown in the status message.

3. Adding a person with missing mandatory fields
   1. Test case: `add p/98765432 tg/@johndoe`<br>
      Expected: No person is added. Error details indicating the name is missing and the correct command format are shown in the status message.

4. Adding a person that duplicates an existing contact
   1. Prerequisites: A person with either the same email, Telegram handle, or phone number already exists in the address book. <br>
      e.g. `add n/Peter Tan p/98765432 e/peter@example.com`

   2. Test case: `add n/peter e/peter@example.com tg/@peter_1011`<br>
      Expected: No new person is added. Error details shown in the status message indicating that a person with the same email already exists.

### Deleting a person

1. Deleting a person while all persons are being shown
   1. Prerequisites: List all persons using the `list` command. Multiple persons are displayed.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous (i.e. 3rd test case above).

### Editing a person

1. Editing a person while all persons are being shown
   1. Prerequisites: List all persons using the `list` command, and at least one person exists in the addressbook.

   2. Test case: `edit 1 a/Blk 400, Tampines Street 4, #12-28`<br>
      Expected: The first contact in the list is updated successfully. Details of the edited contact with the updated address are shown in the status message.

   3. Test case: `edit 1 t/`<br>
      Expected: No changes are made. Error details of tag cannot be empty is shown in the status message.

2. Editing a person in a filtered list
   1. Prerequisites: A person with the name `Bernice` exists. Filter the list using `find n/bernice`.

   2. Test case: `edit 1 t/friend`<br>
      Expected: The first person in the filtered list is updated successfully with a single new tag `friend`. After editing, the full original person list is displayed again.

### Enrolling a person

1. Enrolling a person into a course with tutorial class

    1. Prerequisites:
    - The first person in the contact list is enrolled in the course `CS2103T` and tutorial class `T11`.
    - If the first person is enrolled in `CS2103T` but in different tutorial class, unenroll the person first using `unenroll 1 c/CS2103T` then followed by `enroll 1 c/CS2103T tut/T11`.
    - If not enrolled into any tutorials of `CS2103T`, enroll the first person using `enroll 1 c/CS2103T tut/T11`

    2. Test case: `enroll 1 c/CS2103T tut/T19`<br>
       Expected: The first person is not enrolled into `CS2103T T19`. Error details is shown in the status message indicating that the student is already enrolled in `CS2103T`.

    3. Test case: `enroll 1 c/CS2109S tut/T06`<br>
       Expected: The student is successfully enrolled in course `CS2109S` with tutorial `T06`. The status message shows the student details with the updated course and tutorial enrollment.

### Getting help

1. Finding the list of commands and the link to the user guide
    1. Test case: Press the Help button then click Help in the dropdown, or press F1.<br>
       Expected: A pop-up window appears displaying the list of all available commands and a link to the user guide at the bottom.

### Unenrolling a person

1. Unenrolling a person from a course

    1. Prerequisites:
    - The first person in the contact list is enrolled in the course `CS2103T`.
    - If not enrolled in `CS2103T`, enroll the first person using `enroll 1 c/CS2103T tut/T11`.

    2. Test case: `unenroll 1 c/CS2103T`<br>
       Expected: The first person is successfully unenrolled from the course `CS2103T`. The status message shows the student details with the updated course enrollment.

### Marking attendance

1. Marking attendance for a student in a course

    1. Prerequisites: 
        - At least one person exists in the contact list. Ensure that the student at index 1 is currently enrolled in CS2103T. 
        - If the student is not enrolled in CS2103T, use the `enroll 1 c/CS2103T tut/T01` command before proceeding.
    
    2. Test case: `attend 1 c/CS2103T w/3`<br>
       Expected: The attendance for Week 3 of CS2103T is successfully marked for the student at index 1. A status message is shown indicating that attendance for Week 3 has been marked.
    
    3. Test case: `attend 1 c/CS2103T w/0`<br>
       Expected: Error details indicating that the week must be between 1 and 13 are shown in the status message. No attendance is marked.

### Unmarking attendance

1. Unmarking attendance for a student in a course

    1. Prerequisites: 
        - At least one person exists in the contact list. Ensure that the student at index 1 is currently enrolled in CS2103T. 
        - If the student is not enrolled in CS2103T, use the `enroll 1 c/CS2103T tut/T01` command before proceeding.
    
    2. Test case: `unattend 1 c/CS2103T w/3`<br>
       Expected: The attendance for Week 3 of CS2103T is successfully unmarked for the student at index 1. A status message is shown indicating that attendance for Week 3 has been unmarked.
    
    3. Test case: `unattend 1 c/CS2103T w/0`<br>
       Expected: Error details indicating that the week must be between 1 and 13 are shown in the status message. No attendance is unmarked.

### Unsetting a person's field

1. Unsetting an optional field of a person

    1. Prerequisites:
       - The first person in the contact list has a phone number.
       - If not, add one using `edit 1 p/98765432`.

    2. Test case: `unset 1 p/`<br>
       Expected: The phone number of the first person is removed. Success message showing the previous value is displayed.

    3. Test case: `unset 1 p/` (again when phone is already missing)<br>
       Expected: No changes are made. Status message indicates that the phone number is already missing.

2. Unsetting a mandatory field or course/tutorial

    1. Test case: `unset 1 n/`<br>
       Expected: No changes are made. Error message indicating that the name is a mandatory field and cannot be unset is shown.

    2. Test case: `unset 1 c/`<br>
       Expected: No changes are made. Error message indicating that courses/tutorials cannot be removed using `unset` is shown.

3. Incorrect unset commands

    1. Test case: `unset 1`<br>
       Expected: No changes are made. Error message indicating that exactly one field to unset must be provided is shown.

    2. Test case: `unset 1 p/ a/`<br>
       Expected: No changes are made. Error message indicating that only one field can be unset at a time is shown.

    3. Test case: `unset 1 p/98765432`<br>
       Expected: No changes are made. Error message indicating that `unset` only accepts a field prefix with no value is shown.

### Viewing a person

1. Viewing a person’s full details while all persons are shown in the contact list
   1. Prerequisites: Multiple persons exist in the contact list. List all persons using the `list` command. After executing the `list` command, the full details of the first person in the list are automatically displayed.

   2. Test case: `view 1`<br>
      Expected: The full details of the first person in the currently displayed list are still displayed. A status message is shown to indicate that the user is already viewing the first contact’s full details.

   3. Test case: `view 2`<br>
      Expected: The full details of the second person in the currently displayed list are displayed. A status message is shown to indicate that the user is now viewing the second contact’s full details.

   4. Test case: `view 0`<br>
      Expected: Error details indicating that the index cannot be zero are shown in the status message. The full details of the last viewed contact are still displayed (following the sequence of test cases strictly, this refers to the second person in the list).

### Saving data

1. Dealing with missing data files
   1. Test case: Delete the `addressbook.json` file from `[JAR file location]/data/` if present. Launch the application.<br>
      Expected:
      - The application detects that the data file is missing and launches without any crashes.
      - A default sample contact list containing 7 persons is automatically loaded.

2. Dealing with corrupted data files
   1. Prerequisites: A save file exists at `[JAR file location]/data/addressbook.json`.
   2. Test case: Open `addressbook.json` and corrupt the save file by modifying the first person’s name entry from <br>
        `"name" : "Alex Yeoh"`  to  `"name" : "-"` and launch the application.<br>
      Expected:
      - The application detects that the data file is corrupted and launches without crashing.
      - The application is loaded with an empty contact list.
