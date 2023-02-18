package com.topjava.webapp.storage;

import com.topjava.webapp.exception.ExistStorageException;
import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public void save(Resume r) {
        Object searchKey = getIndex((r.getUuid()));
        if (isExist(searchKey)) {
            throw new ExistStorageException(r.getUuid());
        } else {
            saveElement(r);
        }
    }

    @Override
    public void update(Resume r) {
        Object searchKey = getIndex(r.getUuid());
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            updateElement(searchKey, r);
        }
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getIndex(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return getResume(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getIndex(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteElement(searchKey);
        }
    }

    protected abstract void saveElement(Resume r);

    protected abstract void deleteElement(Object searchKey);

    protected abstract void updateElement(Object searchKey, Resume r);

    protected abstract int getIndex(String uuid);

    protected abstract Resume getResume(Object searchKey);

    protected abstract boolean isExist(Object searchKey);
}
