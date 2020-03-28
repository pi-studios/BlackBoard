package com.pistudiosofficial.myclass.objects;

public class AssignmentObject {
    String description,dueDate, timePosted,title;

    public AssignmentObject(String description, String dueDate, String timePosted, String title) {
        this.description = description;
        this.dueDate = dueDate;
        this.timePosted = timePosted;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
