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
            readMap(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readMap(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSection(dis, sectionType));
            });

            return resume;
        }
    }

    private <T> void writeCollection(Collection<T> collection, DataOutputStream dos, ItemWriter<T> consumer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            consumer.write(t);
        }
    }

    private void readMap(DataInputStream dis, ItemHandler handler) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            handler.handle();
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        return switch (sectionType) {
            case PERSONAL, OBJECTIVE -> new TextSection(dis.readUTF());
            case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(readList(dis, dis::readUTF));
            case EXPERIENCE, EDUCATION -> new CompanySection(readList(dis,
                    () -> new Company(dis.readUTF(), dis.readUTF(), readList(dis,
                            () -> new Company.Period(LocalDate.parse(dis.readUTF()), LocalDate.parse(dis.readUTF()), dis.readUTF(), dis.readUTF())
                    ))));
        };
    }

    private <T> List<T> readList(DataInputStream dis, ItemReader<T> reader) throws IOException {
        int listSize = dis.readInt();
        List<T> list = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            list.add(reader.read());
        }
        return list;
    }

    @FunctionalInterface
    public interface ItemHandler {
        void handle() throws IOException;
    }

    @FunctionalInterface
    public interface ItemWriter<T> {
        void write(T t) throws IOException;
    }

    @FunctionalInterface
    public interface ItemReader<T> {
        T read() throws IOException;
    }
}
