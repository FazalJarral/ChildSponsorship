package com.example.childsponsorship.bean;

import androidx.annotation.NonNull;

public class Transaction {
String sponsor_name;
String sponsor_id;
String collector_name;
String collector_id;
String amount;
String department;
String published_at;
String status;
String collector_token;
String key;

    public Transaction() {
    }

    public Transaction(String sponsor_name, String sponsor_id, String collector_name, String collector_id, String amount, String department, String published_at, String status) {
        this.sponsor_name = sponsor_name;
        this.sponsor_id = sponsor_id;
        this.collector_name = collector_name;
        this.collector_id = collector_id;
        this.amount = amount;
        this.department = department;
        this.published_at = published_at;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCollector_token() {
        return collector_token;
    }

    public void setCollector_token(String collector_token) {
        this.collector_token = collector_token;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSponsor_name() {
        return sponsor_name;
    }

    public void setSponsor_name(String sponsor_name) {
        this.sponsor_name = sponsor_name;
    }

    public String getSponsor_id() {
        return sponsor_id;
    }

    public void setSponsor_id(String sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public String getCollector_name() {
        return collector_name;
    }

    public void setCollector_name(String collector_name) {
        this.collector_name = collector_name;
    }

    public String getCollector_id() {
        return collector_id;
    }

    public void setCollector_id(String collector_id) {
        this.collector_id = collector_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name" + sponsor_name +
                "Collector" +
                collector_name +
                "Amount" + amount;
    }
}
