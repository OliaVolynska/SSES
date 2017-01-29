package com.example.olia.test.Entity;


public class Theme {
    private int idTheme;
    private String titleTheme;

    public Theme(){}

    public Theme(int idTheme, String titleTheme) {
        this.idTheme = idTheme;
        this.titleTheme = titleTheme;
    }

    public Theme(String titleTheme) {
        this.titleTheme = titleTheme;
    }

    public int getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }

    public String getTitleTheme() {
        return titleTheme;
    }

    public void setTitleTheme(String titleTheme) {
        this.titleTheme = titleTheme;
    }
}
