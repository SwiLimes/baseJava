package com.topjava.webapp.storage;

import com.topjava.webapp.exception.NotExistStorageException;
import com.topjava.webapp.model.AbstractSection;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.ListSection;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.model.SectionType;
import com.topjava.webapp.model.TextSection;
import com.topjava.webapp.sql.SqlHelper;

import java.sql.Connection;
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
            insertContacts(r, connection);
            insertSections(r, connection);
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
            deleteContacts(uuid);
            insertContacts(r, connection);

            deleteSections(uuid);
            insertSections(r, connection);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionExecute(connection -> {
            Resume resume;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM resume r " +
                            "LEFT JOIN contact c " +
                            "ON r.uuid = c.resume_uuid " +
                            "WHERE uuid = ?;")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
                do {
                    readContact(rs, resume);
                } while (rs.next());
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM section s WHERE resume_uuid = ?;")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    readSection(rs, resume);
                }
            }

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
        return sqlHelper.transactionExecute(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r ORDER BY full_name, uuid;")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact;")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    readContact(rs, resumes.get(rs.getString("resume_uuid")));
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM section;")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    readSection(rs, resumes.get(rs.getString("resume_uuid")));
                }
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
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void deleteContacts(String uuid) {
        sqlHelper.execute("DELETE FROM contact WHERE resume_uuid = ?;", ps -> {
            ps.setString(1, uuid);
            ps.execute();
            return null;
        });
    }

    private void insertContacts(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?);")) {
            String uuid = resume.getUuid();
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                ps.setString(1, uuid);
                ps.setString(2, contact.getKey().name());
                ps.setString(3, contact.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void readSection(ResultSet rs, Resume resume) throws SQLException {
        SectionType type = SectionType.valueOf(rs.getString("type"));
        switch (type) {
            case PERSONAL, OBJECTIVE -> resume.addSection(type, new TextSection(rs.getString("content")));
            case ACHIEVEMENT, QUALIFICATIONS -> resume.addSection(type, new ListSection(rs.getString("content").split("\n")));
            default -> {
            }
        }
    }

    private void deleteSections(String uuid) {
        sqlHelper.execute("DELETE FROM section WHERE resume_uuid = ?;", ps -> {
            ps.setString(1, uuid);
            ps.execute();
            return null;
        });
    }

    private void insertSections(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO section(resume_uuid, type, content) VALUES (?, ?, ?);")) {
            String uuid = resume.getUuid();
            for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
                SectionType type = section.getKey();

                ps.setString(1, uuid);
                ps.setString(2, type.name());
                switch (type) {
                    case PERSONAL, OBJECTIVE -> ps.setString(3, ((TextSection) section.getValue()).getText());
                    case ACHIEVEMENT, QUALIFICATIONS -> ps.setString(3, String.join("\n", ((ListSection) section.getValue()).getItems()));
                    default -> {
                    }
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
