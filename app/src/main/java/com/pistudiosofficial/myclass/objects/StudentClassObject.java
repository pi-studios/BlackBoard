package com.pistudiosofficial.myclass.objects;

public class StudentClassObject {

    public String roll,facultyEmail, studentUID, classKey;

    public StudentClassObject(String roll,String facultyEmail, String studentUID, String classKey) {
        this.facultyEmail = facultyEmail;
        this.roll = roll;
        this.studentUID = studentUID;
        this.classKey = classKey;
    }

    public StudentClassObject() {
    }

}
