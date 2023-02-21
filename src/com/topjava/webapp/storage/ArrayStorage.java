package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void saveElement(Resume r) {
        storage[size] = r;
    }

    @Override
    protected void deleteElement(int index) {
        if (index != size - 1) {
            storage[index] = storage[size - 1];
        }
        storage[size - 1] = null;
    }
}
