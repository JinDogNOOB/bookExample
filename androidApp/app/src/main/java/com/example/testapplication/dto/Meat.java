package com.example.testapplication.dto;

public class Meat {
    String key;
    int status;
    String cowPtr;
    String workerPtr;
    String pMeatPtr;
    int weight;
    String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCowPtr() {
        return cowPtr;
    }

    public void setCowPtr(String cowPtr) {
        this.cowPtr = cowPtr;
    }

    public String getWorkerPtr() {
        return workerPtr;
    }

    public void setWorkerPtr(String workerPtr) {
        this.workerPtr = workerPtr;
    }

    public String getpMeatPtr() {
        return pMeatPtr;
    }

    public void setpMeatPtr(String pMeatPtr) {
        this.pMeatPtr = pMeatPtr;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
