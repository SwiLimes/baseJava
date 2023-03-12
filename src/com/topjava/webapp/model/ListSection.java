package com.topjava.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {

    private List<String> stringList;

    public ListSection() {
        stringList = null;
    }

    public ListSection(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public void setContent(Object content) {
        this.stringList = (List<String>) content;
    }

    @Override
    public String getContent() {
        if (Objects.isNull(stringList)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : stringList) {
            sb.append('*').append(str);
            sb.append("\n");
        }
        return sb.toString();
    }


}
