package com.topjava.webapp.storage;

import com.topjava.webapp.exception.StorageException;
import com.topjava.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;

    private final SerializableStrategy strategy;

    public PathStorage(String dir, SerializableStrategy strategy) {
        Path directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(strategy, "strategy must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.toAbsolutePath() + " is not directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory.toAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.strategy = strategy;
    }

    @Override
    public void clear() {
        try (Stream<Path> files = Files.list(directory)) {
            files.forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path clear error", directory.getFileName().toString(), e);
        }
    }

    @Override
    public int size() {
        try (Stream<Path> files = Files.list(directory)){
            return (int) files.count();
        } catch (IOException e) {
            throw new StorageException("Path size error", directory.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        try (Stream<Path> files = Files.list(directory)) {
            return files.map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Path doCopyAll error", directory.toAbsolutePath().toString(), e);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory + "/" + uuid);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Could not create Path " + path.toAbsolutePath(), path.getFileName().toString(), e);
        }
        doUpdate(path, r);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume r) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return strategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }
}
