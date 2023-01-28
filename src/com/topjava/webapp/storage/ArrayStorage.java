package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size == storage.length) {
            return;
        }

        String uuid = r.getUuid();
        int index = getResumeIndexInStorage(uuid);

        if(index == -1 && uuid != null) {
            storage[size++] = r;
        } else {
            System.out.printf("Ошибка при сохранении: резюме с uuid - '%s' уже существует\n", uuid);
        }
    }

    public void update(Resume resume) {
        int index = getResumeIndexInStorage(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.printf("Ошибка при обновлении: не удалось найти резюме с uuid - '%s'\n", resume.getUuid());
        }
    }

    public Resume get(String uuid) {
        int index = getResumeIndexInStorage(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.printf("Ошибка при получении: не удалось найти резюме с uuid - '%s'\n", uuid);
        return null;
    }

    public void delete(String uuid) {
        if (size == 0 && uuid.isEmpty()) {
            return;
        }

        int indexForDelete = getResumeIndexInStorage(uuid);
        if (indexForDelete == -1) {
            System.out.printf("Ошибка при удалении: не удалось найти резюме с uuid - '%s'\n", uuid);
            return;
        }

        if (indexForDelete != size - 1) {
            for (int i = indexForDelete + 1; i < size; i++) {
                storage[i - 1] = storage[i];
            }
        }
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int getResumeIndexInStorage(String uuid) {
        for (int index = 0; index < size; index++) {
            if (storage[index].getUuid().equals(uuid)) {
                return index;
            }
        }
        return -1;
    }
}
