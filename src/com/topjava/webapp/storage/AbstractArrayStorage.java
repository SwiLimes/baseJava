package com.topjava.webapp.storage;

import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void doSave(Resume r, Integer index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        saveElement(r);
        size++;
    }

    @Override
    public void doDelete(Integer index) {
        deleteElement(index);
        size--;
    }

    @Override
    protected void doUpdate(Integer searchKey, Resume r) {
        storage[searchKey] = r;
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[ searchKey];
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    protected abstract void saveElement(Resume r);

    protected abstract void deleteElement(int index);

}
