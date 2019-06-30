package com.pistudiosofficial.myclass.objects;

public class StudentClassObject {

    public String roll,facultyEmail, joinCode, studentUID, classKey;

    public StudentClassObject(String roll,String facultyEmail, String joinCode, String studentUID, String classKey) {
        this.facultyEmail = facultyEmail;
        this.roll = roll;
        this.joinCode = joinCode;
        this.studentUID = studentUID;
        this.classKey = classKey;
    }

    public StudentClassObject() {
    }

}
