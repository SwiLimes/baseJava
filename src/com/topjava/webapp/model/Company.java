package com.topjava.webapp.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private final String name;
    private final String website;
    private final List<Period> periods = new ArrayList<>();

    public Company(String name, String website) {
        this.name = name;
        this.website = website;
    }

    public void addRecord(String startDate, String endDate, String title) {
        periods.add(new Period(startDate, endDate, title));
    }

    public void addRecord(String startDate, String endDate, String title, String description) {
        periods.add(new Period(startDate, endDate, title, description));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(website).append("\n");
        for (Period period : periods) {
            sb.append(period).append("\n");
        }
        return sb.toString();
    }

    static class Period {
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        public Period(String startDate, String endDate, String title) {
            this(startDate, endDate, title, "");
        }

        public Period(String startDate, String endDate, String title, String description) {
            this.startDate = LocalDate.parse(startDate, formatter);
            this.endDate = LocalDate.parse(endDate, formatter);
            this.title = title;
            this.description = description;
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(startDate).append('-').append(endDate).append("\n");
            sb.append(title).append("\n");
            if (!description.isEmpty()) {
                sb.append(description);
            }
            return sb.toString();
        }
    }
}
