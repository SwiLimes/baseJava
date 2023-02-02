package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Ошибка при сохранении: хранилище заполнено");
        } else if (getIndex(r.getUuid()) != -1) {
            System.out.printf("Ошибка при сохранении: резюме с uuid - '%s' уже существует\n", r.getUuid());
        } else {
            storage[size++] = r;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if(index == -1) {
            System.out.printf("Ошибка при удалении: не удалось найти резюме с uuid - '%s'\n", uuid);
            return;
        }

        if(index != size) {
            storage[index] = storage[size - 1];
        }
        storage[size - 1] = null;
        size--;
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}
