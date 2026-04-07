package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

public class MainAppTest {

    @Test
    public void initModelManager_corruptedAddressBook_setsStartupMessageAndUsesEmptyAddressBook() {
        MainApp mainApp = new MainApp();
        Path corruptedPath = Path.of("data", "corrupted-addressbook.json");
        Storage storage = new StorageStub(corruptedPath);

        Model model = invokeInitModelManager(mainApp, storage, new UserPrefs());

        assertEquals("Data file at " + corruptedPath + " could not be loaded."
                + " Starting with an empty AddressBook.", getStartupMessage(mainApp));
        assertTrue(model.getAddressBook().getPersonList().isEmpty());
    }

    private String getStartupMessage(MainApp mainApp) {
        try {
            Field startupMessageField = MainApp.class.getDeclaredField("startupMessage");
            startupMessageField.setAccessible(true);
            return (String) startupMessageField.get(mainApp);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Failed to read startupMessage from MainApp", e);
        }
    }

    private Model invokeInitModelManager(MainApp mainApp, Storage storage, UserPrefs userPrefs) {
        try {
            java.lang.reflect.Method initModelManager = MainApp.class.getDeclaredMethod(
                    "initModelManager", Storage.class, seedu.address.model.ReadOnlyUserPrefs.class);
            initModelManager.setAccessible(true);
            return (Model) initModelManager.invoke(mainApp, storage, userPrefs);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Failed to invoke initModelManager from MainApp", e);
        }
    }

    private static class StorageStub implements Storage {
        private final Path addressBookFilePath;

        private StorageStub(Path addressBookFilePath) {
            this.addressBookFilePath = addressBookFilePath;
        }

        @Override
        public Path getUserPrefsFilePath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getAddressBookFilePath() {
            return addressBookFilePath;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            throw new DataLoadingException(new IOException("Corrupted JSON"));
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
            throw new DataLoadingException(new IOException("Corrupted JSON"));
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}
