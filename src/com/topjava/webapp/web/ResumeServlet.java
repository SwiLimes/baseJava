package com.topjava.webapp.web;

import com.topjava.webapp.Config;
import com.topjava.webapp.ResumeTestData;
import com.topjava.webapp.model.AbstractSection;
import com.topjava.webapp.model.Company;
import com.topjava.webapp.model.CompanySection;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.ListSection;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.model.SectionType;
import com.topjava.webapp.model.TextSection;
import com.topjava.webapp.storage.Storage;
import com.topjava.webapp.util.DateUtil;
import com.topjava.webapp.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
    private static Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        storage = Config.get().getStorage();
        storage.clear();
        storage.save(ResumeTestData.createResume(UUID.randomUUID().toString(), "name1"));
        storage.save(ResumeTestData.createResume2(UUID.randomUUID().toString(), "name2"));
        storage.save(ResumeTestData.createResume3(UUID.randomUUID().toString(), "name3"));
        storage.save(ResumeTestData.createResume4(UUID.randomUUID().toString(), "name4"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName");

        boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume resume;
        if (isCreate) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = req.getParameter(type.name());
            String[] values = req.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                resume.getSections().remove(type);
            } else {
                resume.addSection(type, switch (type) {
                    case PERSONAL, OBJECTIVE -> new TextSection(value);
                    case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(value.split("\n"));
                    case EXPERIENCE, EDUCATION -> {
                        List<Company> companies = new ArrayList<>();
                        String[] websites = req.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (HtmlUtil.isEmpty(name)) {
                                List<Company.Period> periods = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] startDates = req.getParameterValues(pfx + "startDate");
                                String[] endDates = req.getParameterValues(pfx + "endDate");
                                String[] titles = req.getParameterValues(pfx + "title");
                                String[] descriptions = req.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        periods.add(new Company.Period(
                                                DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                companies.add(new Company(name, websites[i], periods));
                            }
                        }
                        yield new CompanySection(companies);
                    }
                });
            }
        }
        if (isCreate) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        resp.sendRedirect("resume");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("resumes", storage.getAllSorted());
            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
            return;
        }

        Resume resume;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                resp.sendRedirect("resume");
                return;
            }
            case "view" -> resume = storage.get(uuid);
            case "create" -> resume = Resume.EMPTY;
            case "edit" -> {
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = resume.getSection(type);
                    switch (type) {
                        case PERSONAL, OBJECTIVE -> {
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                        }
                        case ACHIEVEMENT, QUALIFICATIONS -> {
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                        }
                        case EXPERIENCE, EDUCATION -> {
                            CompanySection companySection = (CompanySection) section;
                            List<Company> emptyFirstCompany = new ArrayList<>() {{
                                add(Company.EMPTY);
                            }};

                            if (companySection != null) {
                                for (Company company : companySection.getCompanies()) {
                                    List<Company.Period> emptyFirstPeriod = new ArrayList<>();
                                    emptyFirstPeriod.add(Company.Period.EMPTY);
                                    emptyFirstPeriod.addAll(company.getPeriods());
                                    emptyFirstCompany.add(new Company(company.getName(), company.getWebsite(), emptyFirstPeriod));
                                }
                            }
                            section = new CompanySection(emptyFirstCompany);
                        }
                    }
                    resume.addSection(type, section);
                }
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }

        req.setAttribute("resume", resume);
        req.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(req, resp);
    }
}