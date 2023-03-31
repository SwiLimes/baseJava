package com.topjava.webapp.storage;

import com.topjava.webapp.storage.strategy.DataStreamStrategy;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamStrategy()));
    }
}