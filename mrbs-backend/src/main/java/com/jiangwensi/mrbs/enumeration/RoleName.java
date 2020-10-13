package com.jiangwensi.mrbs.enumeration;

public enum RoleName {
    USER("USER"), SYSADM("SYSADM");

    private String name;

    RoleName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
