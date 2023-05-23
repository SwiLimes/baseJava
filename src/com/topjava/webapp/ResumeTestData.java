package com.topjava.webapp;

import com.topjava.webapp.model.AbstractSection;
import com.topjava.webapp.model.Company;
import com.topjava.webapp.model.CompanySection;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.ListSection;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.model.SectionType;
import com.topjava.webapp.model.TextSection;

import java.time.Month;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;


public class ResumeTestData {

    public static final String UUID1 = UUID.randomUUID().toString();
    public static final String UUID2 = UUID.randomUUID().toString();
    public static final String UUID3 = UUID.randomUUID().toString();
    public static final String UUID4 = UUID.randomUUID().toString();
    public static final String UUID_NOT_EXIST = "Fake";

    public static final Resume RESUME_1;
    public static final Resume RESUME_2;
    public static final Resume RESUME_3;
    public static final Resume RESUME_4;

    static {
        RESUME_1 = createResume(UUID1, "Name1");
        RESUME_2 = createResume(UUID2, "Name2");
        RESUME_3 = createResume(UUID3, "Name3");
        RESUME_4 = createResume(UUID4, "Name4");
    }

    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class) {{
            put(ContactType.PHONE, "+7(921) 855-0482");
            put(ContactType.SKYPE, "skype:grigory.kislin");
            put(ContactType.EMAIL, "gkislin@yandex.ru");
        }};
        resume.setContacts(contacts);

        TextSection personalSection = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");

        TextSection objectiveSection = new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");

        ListSection achievementsSection = new ListSection(Arrays.asList(
                "Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise", "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA). Организация онлайн стажировок и ведение проектов. Более 3500 выпускников."
        ));

        ListSection qualificationsSection = new ListSection(Arrays.asList(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce"
        ));

        Company javaOnline = new Company("Java Online Projects", "https://javaops.ru/");
        List<Company.Period> javaOnlinePeriods = new ArrayList<>() {{
            add(new Company.Period(2022, Month.OCTOBER, "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок."));
        }};
        javaOnline.setPeriods(javaOnlinePeriods);
        Company wrike = new Company("Wrike", "https://www.wrike.com/");
        List<Company.Period> wrikePeriods = new ArrayList<>() {{
            add(new Company.Period(2014, Month.OCTOBER, 2016, Month.JANUARY, "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
        }};
        wrike.setPeriods(wrikePeriods);
        CompanySection experienceSection = new CompanySection(Arrays.asList(javaOnline, wrike));


        Company spbUniversity = new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "https://itmo.ru/");
        List<Company.Period> periods = new ArrayList<>() {{
            add(new Company.Period(1987, Month.SEPTEMBER, 1993, Month.JULY, "Инженер (программист Fortran, C)", ""));
            add(new Company.Period(1993, Month.SEPTEMBER, 1996, Month.JULY, "Аспирантура (программист С, С++)", ""));
        }};
        spbUniversity.setPeriods(periods);
        Company coursera = new Company("Coursera", "https://www.coursera.org/course/progfun");
        List<Company.Period> courseraPeriods = new ArrayList<>() {{
            add(new Company.Period(2011, Month.MARCH, 2011, Month.APRIL, "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", ""));
        }};
        coursera.setPeriods(courseraPeriods);
        Company siemens = new Company("Siemens AG", "http://www.siemens.ru/");
        List<Company.Period> siemensPeriods = new ArrayList<>() {{
            add(new Company.Period(2005, Month.JANUARY, 2005, Month.APRIL, "3 месяца обучения мобильным IN сетям (Берлин)", ""));
        }};
        siemens.setPeriods(siemensPeriods);
        CompanySection educationSection = new CompanySection(Arrays.asList(spbUniversity, coursera, siemens));

        Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class) {{
            put(SectionType.PERSONAL, personalSection);
            put(SectionType.OBJECTIVE, objectiveSection);
            put(SectionType.ACHIEVEMENT, achievementsSection);
            put(SectionType.QUALIFICATIONS, qualificationsSection);
            put(SectionType.EXPERIENCE, experienceSection);
            put(SectionType.EDUCATION, educationSection);
        }};

        resume.setSections(sections);
        return resume;
    }

    public static Resume createResume2(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        TextSection personalSection = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");

        ListSection achievementsSection = new ListSection(Arrays.asList(
                "Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise", "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA). Организация онлайн стажировок и ведение проектов. Более 3500 выпускников."
        ));

        Company javaOnline = new Company("Java Online Projects", "https://javaops.ru/");
        List<Company.Period> javaOnlinePeriods = new ArrayList<>() {{
            add(new Company.Period(2022, Month.OCTOBER, "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок."));
        }};
        javaOnline.setPeriods(javaOnlinePeriods);
        Company wrike = new Company("Wrike", "https://www.wrike.com/");
        List<Company.Period> wrikePeriods = new ArrayList<>() {{
            add(new Company.Period(2014, Month.OCTOBER, 2016, Month.JANUARY, "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
        }};
        wrike.setPeriods(wrikePeriods);
        CompanySection experienceSection = new CompanySection(Arrays.asList(javaOnline, wrike));


        Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class) {{
            put(SectionType.PERSONAL, personalSection);
            put(SectionType.ACHIEVEMENT, achievementsSection);
            put(SectionType.EXPERIENCE, experienceSection);
        }};

        resume.setSections(sections);
        return resume;
    }

    public static Resume createResume3(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class) {{
            put(ContactType.PHONE, "+7(921) 855-0482");
            put(ContactType.SKYPE, "skype:grigory.kislin");
            put(ContactType.EMAIL, "gkislin@yandex.ru");
        }};
        resume.setContacts(contacts);

        return resume;
    }

    public static Resume createResume4(String uuid, String fullName) {
        return new Resume(uuid, fullName);
    }


    public static void main(String[] args) {
        Resume resume = createResume(UUID.randomUUID().toString(), "Григорий Кислин");

        System.out.println("UUID: " + resume.getUuid() + " | " + " Name: " + resume.getFullName());

        for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
            System.out.println(contact.getKey().getTitle() + ": " + contact.getValue());
        }

        System.out.println();

        for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
            System.out.println(section.getKey().getTitle() + "\n" + section.getValue() + "\n");
        }
    }

}
