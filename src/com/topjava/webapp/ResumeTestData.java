package com.topjava.webapp;

import com.topjava.webapp.model.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class ResumeTestData {

    public static void initResume(Resume resume) {
        Map<ContactType, String> contacts = new EnumMap<ContactType, String>(ContactType.class) {{
            put(ContactType.PHONE, "+7(921) 855-0482");
            put(ContactType.SKYPE, "skype:grigory.kislin");
            put(ContactType.EMAIL, "gkislin@yandex.ru");
        }};
        resume.setContacts(contacts);

        TextSection personalSection = new TextSection();
        personalSection.setText("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");

        TextSection objectiveSection = new TextSection();
        objectiveSection.setText("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");

        ListSection achievementsSection = new ListSection();
        achievementsSection.setStringList(Arrays.asList(
                "Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise", "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA). Организация онлайн стажировок и ведение проектов. Более 3500 выпускников."
        ));

        ListSection qualificationsSection = new ListSection();
        qualificationsSection.setStringList(Arrays.asList(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce"
        ));

        CompanySection experienceSection = new CompanySection();

        Company javaOnline = new Company("Java Online Projects", "https://javaops.ru/");
        List<Company.Period> javaOnlinePeriods = new ArrayList<Company.Period>() {{
            add(new Company.Period("01/10/2023", "20/02/2023", "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок."));
        }};
        javaOnline.setPeriods(javaOnlinePeriods);

        Company wrike = new Company("Wrike", "https://www.wrike.com/");
        List<Company.Period> wrikePeriods = new ArrayList<Company.Period>() {{
            add(new Company.Period("02/10/2014", "14/01/2016", "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
        }};
        wrike.setPeriods(wrikePeriods);

        experienceSection.setCompanies(Arrays.asList(javaOnline, wrike));

        CompanySection educationSection = new CompanySection();

        Company spbUniversity = new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "https://itmo.ru/");
        List<Company.Period> periods = new ArrayList<Company.Period>() {{
            add(new Company.Period("01/09/1987", "05/07/1993", "Инженер (программист Fortran, C)"));
            add(new Company.Period("15/09/1993", "09/07/1996", "Аспирантура (программист С, С++)"));
        }};
        spbUniversity.setPeriods(periods);

        Company coursera = new Company("Coursera", "https://www.coursera.org/course/progfun");
        List<Company.Period> courseraPeriods = new ArrayList<Company.Period>() {{
            add(new Company.Period("02/03/2011", "15/04/2011", "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'"));
        }};
        coursera.setPeriods(courseraPeriods);

        Company siemens = new Company("Siemens AG", "http://www.siemens.ru/");
        List<Company.Period> siemensPeriods = new ArrayList<Company.Period>() {{
            add(new Company.Period("04/01/2005", "25/04/2005", "3 месяца обучения мобильным IN сетям (Берлин)"));
        }};
        siemens.setPeriods(siemensPeriods);

        educationSection.setCompanies(Arrays.asList(spbUniversity, coursera, siemens));

        Map<SectionType, AbstractSection> sections = new EnumMap<SectionType, AbstractSection>(SectionType.class) {{
            put(SectionType.PERSONAL, personalSection);
            put(SectionType.OBJECTIVE, objectiveSection);
            put(SectionType.ACHIEVEMENT, achievementsSection);
            put(SectionType.QUALIFICATIONS, qualificationsSection);
            put(SectionType.EXPERIENCE, experienceSection);
            put(SectionType.EDUCATION, educationSection);
        }};

        resume.setSections(sections);
    }


    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");
        initResume(resume);

        for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
            System.out.println(contact.getKey().getTitle() + ": " + contact.getValue());
        }

        System.out.println();

        for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
            System.out.println(section.getKey().getTitle() + "\n" + section.getValue() + "\n");
        }
    }

}
