package com.topjava.webapp.model;

import java.util.Objects;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.EnumMap;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private List<String> contacts;
    private static final Map<SectionType, AbstractSection> sections = new EnumMap<SectionType, AbstractSection>(SectionType.class) {{
        put(SectionType.PERSONAL, new TextSection());
        put(SectionType.OBJECTIVE, new TextSection());
        put(SectionType.ACHIEVEMENT, new ListSection());
        put(SectionType.QUALIFICATIONS, new ListSection());
        put(SectionType.EXPERIENCE, new CompanySection());
        put(SectionType.EDUCATION, new CompanySection());
    }};

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(uuid, "full name must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public Map<SectionType, AbstractSection> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }
}
