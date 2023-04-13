package com.topjava.webapp.storage;

import com.topjava.webapp.Config;


public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}