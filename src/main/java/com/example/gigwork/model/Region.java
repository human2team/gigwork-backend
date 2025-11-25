package com.example.gigwork.model;

public class Region {
    private String code;
    private String name;
    private String sido;
    private String sgg;
    private String umd;

    public Region(String code, String name, String sido, String sgg, String umd) {
        this.code = code;
        this.name = name;
        this.sido = sido;
        this.sgg = sgg;
        this.umd = umd;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getSido() { return sido; }
    public String getSgg() { return sgg; }
    public String getUmd() { return umd; }
}
