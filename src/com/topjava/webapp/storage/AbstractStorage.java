package com.topjava.webapp.storage;

import com.topjava.webapp.exception.ExistStorageException;
import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public void save(Resume r) {
        if (r.getUuid() == null || r.getUuid().isEmpty()) {
            throw new StorageException("Empty uuid", null);
        } else if (getIndex(r.getUuid()) >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            saveElement(r);
        }
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            updateElement(index, r);
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getResume(index);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteElement(index);
        }
    }

    protected abstract void saveElement(Resume r);

    protected abstract void deleteElement(int index);

    protected abstract void updateElement(int index, Resume r);

    protected abstract int getIndex(String uuid);

    protected abstract Resume getResume(int index);
}
