package com.wrx.quickeats.bean;

public class FilterBean {

    private String filterName;
    private String number;
    private boolean isSelected;

    public FilterBean(String filterName, String number,boolean selected) {
        this.filterName = filterName;
        this.number = number;
        this.isSelected = selected;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
