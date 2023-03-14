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

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return Objects.equals(stringList, that.stringList);
    }

    @Override
    public int hashCode() {
        return stringList != null ? stringList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return stringList.toString();
    }
}
