package com.topjava.webapp.storage;

import com.topjava.webapp.Config;


public class SqlStorageTest extends AbstractStorageTest {
    private static final Config cfg = Config.get();
    private static final String URL = cfg.getDbUrl();
    private static final String USER = cfg.getDbUser();
    private static final String PASS = cfg.getDbPass();

    public SqlStorageTest() {
        super(new SqlStorage(URL, USER, PASS));
    }
}