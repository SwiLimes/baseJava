package com.topjava.webapp.util;

import com.topjava.webapp.model.AbstractSection;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.model.TextSection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.topjava.webapp.ResumeTestData.RESUME_1;

public class JsonParserTest {

    @Test
    public void testResume() {
        String json = JsonParser.write(RESUME_1);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assertions.assertEquals(RESUME_1, resume);
    }

    @Test
    public void write() {
        AbstractSection section1 = new TextSection("TextSection");
        String json = JsonParser.write(section1, AbstractSection.class);
        System.out.println(json);
        AbstractSection section2 = JsonParser.read(json, AbstractSection.class);
        Assertions.assertEquals(section1, section2);
    }
}
