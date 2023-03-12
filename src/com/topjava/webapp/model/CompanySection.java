package com.topjava.webapp.model;

import java.util.List;

public class CompanySection extends AbstractSection {

    private List<Company> companies;

    public CompanySection() {
        this.companies = null;
    }

    public CompanySection(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public void setContent(Object companies) {
        this.companies = (List<Company>) companies;
    }

    @Override
    public String getContent() {
        if (companies.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Company company : companies) {
            sb.append(company).append("\n");
        }
        return sb.toString();
    }


}
