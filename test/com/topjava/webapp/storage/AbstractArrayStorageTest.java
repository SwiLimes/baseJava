package com.topjava.webapp.storage;

import com.topjava.webapp.exception.ExistStorageException;
import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID1));
        storage.save(new Resume(UUID2));
        storage.save(new Resume(UUID3));
    }

    @Test
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    void getAll() {
        Resume[] resumes = this.storage.getAll();
        for (Resume resume : resumes) {
            String uuid = resume.getUuid();
            Assertions.assertEquals(storage.get(uuid), resume);
        }
    }

    @Test
    void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    void save() {
        String uuid = "uuid4";
        Resume resume = new Resume(uuid);
        storage.save(resume);
        Resume[] resumes = storage.getAll();

        Assertions.assertEquals(4, storage.size());
        Assertions.assertTrue(Arrays.asList(resumes).contains(resume));

    }

    @Test
    void delete() {
        Resume resume = storage.get(UUID1);
        storage.delete(UUID1);
        Resume[] resumes = storage.getAll();

        Assertions.assertEquals(2, storage.size());
        Assertions.assertFalse(Arrays.asList(resumes).contains(resume));
    }

    @Test
    void get() {
        Resume resume = new Resume(UUID1);
        Assertions.assertEquals(resume, storage.get(UUID1));
    }

    @Test
    void update() {
        Resume resume = storage.get(UUID2);
        storage.update(resume);

        Assertions.assertEquals(resume, storage.get(UUID2));
    }

    @Test
    void getExist() {
        Assertions.assertThrows(ExistStorageException.class, () -> {
            storage.save(new Resume(UUID1));
        });
    }

    @Test
    void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.delete("test");
        });
    }

    @Test
    void getStorageOverflow() {
        try {
            for (int i = 0; i < 9997; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException exception) {
            Assertions.fail("Overflow came early");
        }

        Throwable exception = Assertions.assertThrows(StorageException.class, () -> {
            storage.save(new Resume());
        });
        Assertions.assertEquals("Storage overflow", exception.getMessage());
    }

    @Test
    void getEmptyUuid() {
        Throwable exception = Assertions.assertThrows(StorageException.class, () -> {
            storage.save(new Resume(""));
        });
        Assertions.assertEquals("Empty uuid", exception.getMessage());
    }
}