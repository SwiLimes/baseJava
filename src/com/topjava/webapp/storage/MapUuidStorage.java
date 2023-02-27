package com.topjava.webapp.storage;

import com.topjava.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getListResumes() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object getSearchKey(String uuid) {
        Object searchKey = storage.get(uuid);
        return Objects.isNull(searchKey) ? null : uuid;
    }

    @Override
    protected void doSave(Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Object uuid) {
        storage.remove(uuid);
    }

    @Override
    protected void doUpdate(Object uuid, Resume r) {
        storage.replace((String) uuid, r);
    }

    @Override
    protected Resume doGet(Object uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object uuid) {
        return storage.containsKey(uuid);
    }
}
