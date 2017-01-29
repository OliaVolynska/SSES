package com.example.olia.test.Entity;

public class Test {
    private int idTest;
    private String titleTest;
    private float maximalMark;
    private String themes;

    public Test(){
    }

    public Test(int idTest, String titleTest, float maximalMark, String themes) {
        this.idTest = idTest;
        this.titleTest = titleTest;
        this.maximalMark = maximalMark;
        this.themes = themes;
    }

    public Test(String titleTest, float maximalMark, String themes) {
        this.titleTest = titleTest;
        this.maximalMark = maximalMark;
        this.themes = themes;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public String getTitleTest() {
        return titleTest;
    }

    public void setTitleTest(String titleTest) {
        this.titleTest = titleTest;
    }

    public float getMaximalMark() {
        return maximalMark;
    }

    public void setMaximalMark(float maximalMark) {
        this.maximalMark = maximalMark;
    }

    public String getThemes() {
        return themes;
    }

    public void setThemes(String themes) {
        this.themes = themes;
    }

    public String[] getArrThemes(){
        String strThemes[] = themes.split("\n");
        String themesArr[] = new String[strThemes.length];
        System.arraycopy(strThemes, 0, themesArr, 0, strThemes.length);
        return themesArr;
    }
}
