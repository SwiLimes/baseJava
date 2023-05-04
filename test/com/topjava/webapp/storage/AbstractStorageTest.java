package com.topjava.webapp.storage;

import com.topjava.webapp.Config;
import com.topjava.webapp.exception.ExistStorageException;
import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.topjava.webapp.ResumeTestData.*;

public class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected final Storage storage;

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

        List<Resume> expected = new ArrayList<>();
        Assertions.assertIterableEquals(expected, storage.getAllSorted());
    }

    @Test
    void getAllSorted() {
        List<Resume> expected = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
        Assertions.assertIterableEquals(expected, storage.getAllSorted());
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
        Resume resume = new Resume(UUID2, "New Name");
        resume.addContact(ContactType.SKYPE, "kas@");
        resume.addContact(ContactType.EMAIL, "kas@mail.ru");
        storage.update(resume);
        Assertions.assertEquals(resume, storage.get(UUID2));
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
}