package com.topjava.webapp.storage.strategy;

import com.topjava.webapp.model.AbstractSection;
import com.topjava.webapp.model.Company;
import com.topjava.webapp.model.CompanySection;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.ListSection;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.model.SectionType;
import com.topjava.webapp.model.TextSection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamStrategy implements SerializableStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            writeCollection(r.getContacts().entrySet(), dos, contact -> {
                dos.writeUTF(contact.getKey().name());
                dos.writeUTF(contact.getValue());
            });

            writeCollection(r.getSections().entrySet(), dos, section -> {
                SectionType sectionType = section.getKey();
                AbstractSection abstractSection = section.getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) abstractSection).getText());
                    case ACHIEVEMENT, QUALIFICATIONS -> writeCollection(((ListSection) abstractSection).getItems(), dos, dos::writeUTF);
                    case EXPERIENCE, EDUCATION -> writeCollection(((CompanySection) abstractSection).getCompanies(), dos, company -> {
                        dos.writeUTF(company.getName());
                        dos.writeUTF(company.getWebsite());
                        writeCollection(company.getPeriods(), dos, period -> {
                            dos.writeUTF(period.getStartDate().toString());
                            dos.writeUTF(period.getEndDate().toString());
                            dos.writeUTF(period.getTitle());
                            dos.writeUTF(period.getDescription());
                        });
                    });
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int contactsSize = dis.readInt();
            for (int c = 0; c < contactsSize; c++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int s = 0; s < sectionsSize; s++) {
                SectionType type = SectionType.valueOf(dis.readUTF());
                switch (type) {
                    case PERSONAL, OBJECTIVE -> resume.addSection(type, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        int itemsSize = dis.readInt();
                        List<String> items = new ArrayList<>(itemsSize);
                        for (int i = 0; i < itemsSize; i++) {
                            items.add(dis.readUTF());
                        }
                        resume.addSection(type, new ListSection(items));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        int companiesSize = dis.readInt();
                        List<Company> companies = new ArrayList<>(companiesSize);
                        for (int c = 0; c < companiesSize; c++) {
                            String name = dis.readUTF();
                            String website = dis.readUTF();
                            int periodsSize = dis.readInt();
                            List<Company.Period> periods = new ArrayList<>(periodsSize);
                            for (int p = 0; p < periodsSize; p++) {
                                periods.add(new Company.Period(LocalDate.parse(dis.readUTF()),
                                        LocalDate.parse(dis.readUTF()), dis.readUTF(), dis.readUTF()));
                            }
                            companies.add(new Company(name, website, periods));
                        }
                        resume.addSection(type, new CompanySection(companies));
                    }
                }
            }
            return resume;
        }
    }

    private <T> void writeCollection(Collection<T> collection, DataOutputStream dos, ThrowableConsumer<T> consumer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            consumer.write(t);
        }
    }

    @FunctionalInterface
    public interface ThrowableConsumer<T> {
        void write(T t) throws IOException;
    }
}
