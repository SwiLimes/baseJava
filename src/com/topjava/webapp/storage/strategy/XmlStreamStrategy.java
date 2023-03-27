package com.topjava.webapp.storage.strategy;

import com.topjava.webapp.model.*;
import com.topjava.webapp.util.XmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class XmlStreamStrategy implements SerializableStrategy {

    private final XmlParser xmlParser;

    public XmlStreamStrategy() {
        xmlParser = new XmlParser(Resume.class, TextSection.class, ListSection.class, CompanySection.class,
                Company.class, Company.Period.class);
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try(Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(Reader reader = new InputStreamReader(is)) {
            return xmlParser.unmarshall(reader);
        }
    }
}
