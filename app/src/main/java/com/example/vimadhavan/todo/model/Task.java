package com.example.vimadhavan.todo.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by vimadhavan on 5/18/2017.
 */

public class Task implements Comparable<Task> {
    private String title,description;
    private Integer id,status,date,month,year;
    private Date dateValue=new Date();

    public Task() {
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Task(String title, String description, Integer date, Integer month, Integer year) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.month = month;
        this.year = year;

        dateValue.setDate(date);
        dateValue.setMonth(month-1);
        dateValue.setYear(year);

        this.status=0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTargetDate() {
        return this.year+"/"+(this.month)+"/"+this.date;
    }

    public void setTargetDate(String targetDate) {
        this.year= Integer.valueOf(targetDate.split("/")[0]);
        this.month=Integer.valueOf(targetDate.split("/")[1]);
        this.date=Integer.valueOf(targetDate.split("/")[2]);


        dateValue.setDate(date);
        dateValue.setMonth(month-1);
        dateValue.setYear(year);


    }

    public void updateDateTime(){
        dateValue.setDate(date);
        dateValue.setMonth(month-1);
        dateValue.setYear(year);
    }

    public Date getDateTime(){
        return dateValue;
    }


    @Override
    public int compareTo(@NonNull Task o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
