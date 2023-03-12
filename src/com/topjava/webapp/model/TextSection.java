package com.topjava.webapp.model;

public class TextSection extends AbstractSection {

    private String text;

    public TextSection() {
        this.text = "";
    }

    public TextSection(String text) {
        this.text = text;
    }

    @Override
    public void setContent(Object text) {
        this.text = (String) text;
    }

    @Override
    public String getContent() {
        return text;
    }


}
