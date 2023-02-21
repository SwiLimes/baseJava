package com.topjava.webapp.storage;

import com.topjava.webapp.exception.ExistStorageException;
import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

class AbstractStorageTest {
    private final Storage storage;

    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";
    private static final String UUID_NOT_EXIST = "Fake";
    private static final Resume RESUME_1 = new Resume(UUID1);
    private static final Resume RESUME_2 = new Resume(UUID2);
    private static final Resume RESUME_3 = new Resume(UUID3);
    private static final Resume RESUME_4 = new Resume(UUID4);


    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }

    void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);

        Resume[] expected = {};
        Assertions.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    void getAll() {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        Assertions.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void save() {
        storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test
    void delete() {
        Resume resume = storage.get(UUID1);
        storage.delete(UUID1);
        assertSize(2);
        Assertions.assertThrows(NotExistStorageException.class, () -> assertGet(resume));
    }

    @Test
    void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    void update() {
        Resume resume = storage.get(UUID2);
        storage.update(resume);
        Assertions.assertSame(resume, storage.get(UUID2));
    }

    @Test
    void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_NOT_EXIST));
    }

    @Test
    void saveExist() {
        Assertions.assertThrows(ExistStorageException.class, () -> storage.save(RESUME_2));
    }

    @Test
    void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_NOT_EXIST));
    }

    @Test
    void updateNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.update(RESUME_4));
    }

    private boolean isListOrMapStorage() {
        return storage instanceof ListStorage || storage instanceof MapStorage;
    }


    @DisabledIf("isListOrMapStorage")
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