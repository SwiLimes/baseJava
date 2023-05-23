package com.topjava.webapp.web;

import com.topjava.webapp.Config;
import com.topjava.webapp.ResumeTestData;
import com.topjava.webapp.model.ContactType;
import com.topjava.webapp.model.ListSection;
import com.topjava.webapp.model.Resume;
import com.topjava.webapp.model.SectionType;
import com.topjava.webapp.model.TextSection;
import com.topjava.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
    private static  Storage storage;

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
        Resume resume = new Resume(uuid, fullName);
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
            if (value != null && value.trim().length() != 0) {
                resume.addSection(type, switch (type) {
                    case PERSONAL, OBJECTIVE -> new TextSection(value);
                    case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(value.split("\n"));
                    case EXPERIENCE, EDUCATION -> new TextSection("miss");
                });
            } else {
                resume.getSections().remove(type);
            }
        }
        storage.update(resume);
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
            case "view", "edit" -> resume = storage.get(uuid);
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }

        req.setAttribute("resume", resume);
        req.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(req, resp);
    }
}