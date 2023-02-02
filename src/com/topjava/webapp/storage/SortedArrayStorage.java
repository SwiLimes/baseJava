package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if(r.getUuid() == null || r.getUuid().isEmpty()) {
            return;
        } else if (size == STORAGE_LIMIT) {
            System.out.println("Ошибка при сохранении: хранилище заполнено");
        } else {
            int index = getIndex(r.getUuid());
            if (index >= 0) {
                System.out.printf("Ошибка при сохранении: резюме с uuid - '%s' уже существует\n", r.getUuid());
            } else {
                index = Math.abs(index) - 1;
                System.arraycopy(storage, index, storage, index + 1, size - index);
                storage[index] = r;
                size++;
            }
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.printf("Ошибка при удалении: не удалось найти резюме с uuid - '%s'\n", uuid);
            return;
        }
        System.arraycopy(storage, index + 1, storage, index, size - index);
        storage[size] = null;
        size--;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
