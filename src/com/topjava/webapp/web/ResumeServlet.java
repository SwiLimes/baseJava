package com.topjava.webapp.web;

import com.topjava.webapp.Config;
import com.topjava.webapp.ResumeTestData;
import com.topjava.webapp.model.Resume;
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
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
        storage = Config.get().getStorage();
        storage.clear();
        storage.save(ResumeTestData.createResume(UUID.randomUUID().toString(), "name1"));
        storage.save(ResumeTestData.createResume(UUID.randomUUID().toString(), "name2"));
        storage.save(ResumeTestData.createResume(UUID.randomUUID().toString(), "name3"));
        storage.save(ResumeTestData.createResume(UUID.randomUUID().toString(), "name4"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        resp.getWriter().write(getResumesTableMarkup(req.getParameter("uuid")));
    }

    private String getResumesTableMarkup(String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/resumes-table.css\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table>\n" +
                "<tr>\n" +
                "<th>UUID</th>\n" +
                "<th>Full Name</th>\n" +
                "</tr>\n");
        if (uuid != null) {
            sb.append(getResumeMarkup(storage.get(uuid)));
        } else {
            storage.getAllSorted().forEach(resume -> sb.append(getResumeMarkup(resume)));
        }
        sb.append("</table>\n" +
                "</body>\n" +
                "</html>");

        return sb.toString();
    }

    private String getResumeMarkup(Resume resume) {
        return "<tr>\n" +
                "<td>" + resume.getUuid() + "</td>\n" +
                "<td>" + resume.getFullName() + "</td>\n" +
                "</tr>\n";
    }
}