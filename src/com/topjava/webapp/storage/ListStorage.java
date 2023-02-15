package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.LinkedList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private static final List<Resume> storage = new LinkedList<>();
    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = storage.toArray(new Resume[0]);
        return resumes;
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
    protected void deleteElement(int index) {
        storage.remove(index);
    }

    @Override
    protected void updateElement(int index, Resume r) {
        storage.set(index, r);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume resume = new Resume(uuid);
        if (storage.contains(resume)) {
            return storage.indexOf(resume);
        }
        return -1;
    }

    @Override
    protected Resume getResume(int index) {
        return storage.get(index);
    }
}
