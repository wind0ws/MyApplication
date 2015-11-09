package com.michael.jiang.listviewloaddemo.entity;

/**
 *  Apk包信息 Created by Jiang on 2015/3/25.
 */
public class ApkEntity {

    public String getName() {
        return _Name;
    }

    public String getDescription() {
        return _Description;
    }

    public String getInfo() {
        return _Info;
    }

    public void setName(String _Name) {
        this._Name = _Name;
    }

    public void setDescription(String _Description) {
        this._Description = _Description;
    }

    public void setInfo(String _Info) {
        this._Info = _Info;
    }

    private String _Name;
    private String _Description;
    private String _Info;
}
