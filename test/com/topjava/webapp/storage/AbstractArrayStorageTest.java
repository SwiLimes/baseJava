package com.topjava.webapp.storage;

import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractArrayStorageTest extends AbstractStorageTest {
    Storage storage;
    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
        this.storage = storage;
    }

    @Test
    void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException exception) {
            Assertions.fail("Overflow came early");
        }

        Throwable exception = Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume()));
        Assertions.assertEquals("Storage overflow", exception.getMessage());
    }
}
