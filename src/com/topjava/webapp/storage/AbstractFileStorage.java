package com.topjava.webapp.storage;

import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    public AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                resumes.add(doRead(file));
            }
        }
        return resumes;
    }

    @Override
    public int size() {
        return Objects.requireNonNull(directory.list()).length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("Failed to create file", r.getUuid(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        doWrite(r, file);
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    protected abstract void doWrite(Resume r, File file);
    protected abstract Resume doRead(File file);
}
