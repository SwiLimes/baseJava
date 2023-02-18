package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.LinkedList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new LinkedList<>();
    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void saveElement(Resume r) {
        storage.add(r);
    }

    @Override
    protected void deleteElement(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected void updateElement(Object searchKey, Resume r) {
        storage.set((int) searchKey, r);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume resume = new Resume(uuid);
        return storage.indexOf(resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey < 0 ? false : true;
    }
}
