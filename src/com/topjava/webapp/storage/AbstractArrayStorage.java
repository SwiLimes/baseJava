package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Ошибка при сохранении: хранилище заполнено");
        } else if (r.getUuid() == null || r.getUuid().isEmpty()) {
            System.out.println("Ошибка при сохранении: передано пустое значение");
        } else if (getIndex(r.getUuid()) >= 0) {
            System.out.printf("Ошибка при сохранении: резюме с uuid - '%s' уже существует\n", r.getUuid());
        } else {
            saveElement(r);
            size++;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.printf("Ошибка при удалении: не удалось найти резюме с uuid - '%s'\n", uuid);
        } else {
            deleteElement(index);
            size--;
        }

    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.printf("Ошибка при получении: не удалось найти резюме с uuid - '%s'\n", uuid);
            return null;
        }
        return storage[index];
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            System.out.printf("Ошибка при обновлении: не удалось найти резюме с uuid - '%s'\n", resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    protected abstract void saveElement(Resume r);

    protected abstract void deleteElement(int index);
    protected abstract int getIndex(String uuid);
}
