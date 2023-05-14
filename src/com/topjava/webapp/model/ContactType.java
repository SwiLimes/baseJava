package com.topjava.webapp.model;

public enum ContactType {
    PHONE("Телефон"),
    SKYPE("Skype") {
        @Override
        protected String toHtml0(String value) {
            return getTitle() + ": " + toLink("skype:" + value, value);
        }
    },
    EMAIL("Почта") {
        @Override
        protected String toHtml0(String value) {
            return getTitle() + ": " + toLink("mailto:" + value, value);
        }
    },
    LINKED_IN("Профиль LinkedIn") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    },
    GITHUB("Профиль GitHub") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    },
    STACKOVERFLOW("Профиль Stackoverflow") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    },
    HOMEPAGE("Домашняя страница") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    };

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
        return value != null ? toHtml0(value) : "";
    }

    public String toLink(String href) {
        return toLink(href, title);
    }

    public String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}
