package com.topjava.webapp.storage;

import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPass) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPass));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume;", ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionExecute(connection -> {
            String uuid = r.getUuid();
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?);")) {
                ps.setString(1, uuid);
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?);")) {
                for (Map.Entry<ContactType, String> contact : r.getContacts().entrySet()) {
                    ps.setString(1, uuid);
                    ps.setString(2, contact.getKey().name());
                    ps.setString(3, contact.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionExecute(connection -> {
            String uuid = r.getUuid();
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?;")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            for (Map.Entry<ContactType, String> contact : r.getContacts().entrySet()) {
                try (PreparedStatement ps = connection.prepareStatement("UPDATE contact SET value = ? WHERE resume_uuid = ? AND type = ?;")) {
                    ps.setString(1, contact.getValue());
                    ps.setString(2, uuid);
                    ps.setString(3, contact.getKey().name());
                    ps.execute();
                }
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute(
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE uuid = ?;", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                readContact(rs, resume);
            } while (rs.next());

            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?;", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute(
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "ORDER BY full_name, uuid;", ps -> {
                    Map<String, Resume> resumes = new LinkedHashMap<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        if (!resumes.containsKey(uuid)) {
                            resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                        }
                        readContact(rs, resumes.get(uuid));
                    }
                    return new ArrayList<>(resumes.values());
                });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume;", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("count") : 0;
        });
    }

    private void readContact(ResultSet rs, Resume r) throws SQLException {
        ContactType type = ContactType.valueOf(rs.getString("type"));
        String value = rs.getString("value");
        r.addContact(type, value);
    }
}
