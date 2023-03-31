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
import java.util.List;
import java.util.Map;

public class DataStreamStrategy implements SerializableStrategy {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) entry.getValue()).getText());
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> items = ((ListSection) entry.getValue()).getItems();
                        dos.writeInt(items.size());
                        for (String item : items) {
                            dos.writeUTF(item);
                        }
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Company> companies = ((CompanySection) entry.getValue()).getCompanies();
                        dos.writeInt(companies.size());
                        for (Company company : companies) {
                            dos.writeUTF(company.getName());
                            dos.writeUTF(company.getWebsite());
                            List<Company.Period> periods = company.getPeriods();
                            dos.writeInt(periods.size());
                            for (Company.Period period : periods) {
                                dos.writeUTF(period.getStartDate().toString());
                                dos.writeUTF(period.getEndDate().toString());
                                dos.writeUTF(period.getTitle());
                                dos.writeUTF(period.getDescription());
                            }
                        }
                    }
                }
            }
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
}
