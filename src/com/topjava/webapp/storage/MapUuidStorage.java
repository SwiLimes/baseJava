package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage<String> {
    private final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doSave(Resume r, String uuid) {
        storage.put(uuid, r);
    }

    @Override
    protected void doDelete(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected void doUpdate(String uuid, Resume r) {
        storage.put(uuid, r);
    }

    @Override
    protected Resume doGet(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(String uuid) {
        return storage.containsKey(uuid);
    }
}
