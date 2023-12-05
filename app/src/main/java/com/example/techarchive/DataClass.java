package com.example.techarchive;

public class DataClass {

    private String dataName;
    private String dataGuest;
    private String dataInfo;
    private String dataImage;

    public String getDataName() {
        return dataName;
    }

    public String getDataGuest() {
        return dataGuest;
    }

    public String getDataInfo() {
        return dataInfo;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataName, String dataGuest, String dataInfo, String dataImage) {
        this.dataName = dataName;
        this.dataGuest = dataGuest;
        this.dataInfo = dataInfo;
        this.dataImage = dataImage;
    }
}