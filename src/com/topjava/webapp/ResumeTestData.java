package com.topjava.webapp;

import com.topjava.webapp.model.*;

import java.util.Arrays;
import java.util.Map;

public class ResumeTestData {

    public static void initSections(Resume resume) {
        resume.setContacts(Arrays.asList("Skype: skype:grigory.kislin", "Почта: gkislin@yandex.ru", "Профиль LinkedIn"));
        Company company = new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "https://itmo.ru/");
        company.addRecord("01/09/1987", "05/07/1993", "Инженер (программист Fortran, C)");
        company.addRecord("15/09/1993", "09/07/1996", "Аспирантура (программист С, С++)");
        Map<SectionType, AbstractSection> sections = resume.getSections();

        sections.get(SectionType.PERSONAL).setContent("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        sections.get(SectionType.OBJECTIVE).setContent("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        sections.get(SectionType.ACHIEVEMENT).setContent(Arrays.asList(
                "Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise", "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA). Организация онлайн стажировок и ведение проектов. Более 3500 выпускников."
        ));
        sections.get(SectionType.QUALIFICATIONS).setContent(Arrays.asList(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce"
        ));

        Company javaOnline = new Company("Java Online Projects", "https://javaops.ru/");
        javaOnline.addRecord("01/10/2023", "20/02/2023", "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.");
        Company wrike = new Company("Wrike", "https://www.wrike.com/");
        wrike.addRecord("02/10/2014", "14/01/2016", "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        sections.get(SectionType.EXPERIENCE).setContent(Arrays.asList(javaOnline, wrike));

        Company coursera = new Company("Coursera", "https://www.coursera.org/course/progfun");
        coursera.addRecord("02/03/2011", "15/04/2011", "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'");
        Company siemens = new Company("Siemens AG", "http://www.siemens.ru/");
        siemens.addRecord("04/01/2005", "25/04/2005", "3 месяца обучения мобильным IN сетям (Берлин)");
        sections.get(SectionType.EDUCATION).setContent(Arrays.asList(coursera, siemens));
    }


    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");
        initSections(resume);

        for (String contact : resume.getContacts()) {
            System.out.println(contact);
        }

        System.out.println();

        for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
            System.out.println(section.getKey().getTitle() + "\n" + section.getValue().getContent() + "\n");
        }
    }

}
