package com.pistudiosofficial.myclass.objects;

import java.io.Serializable;

public class ClassObject implements Serializable {
    public String facultyName, facultyEmail, facultyUID,
            className, joinCode, sessionStart,
            sessionEnd, startRoll, endRoll;

    public ClassObject(String facultyName,String facultyEmail, String facultyUID, String className, String joinCode, String sessionStart, String sessionEnd, String startRoll, String endRoll) {
        this.facultyEmail=facultyEmail;
        this.facultyName = facultyName;
        this.facultyUID = facultyUID;
        this.className = className;
        this.joinCode = joinCode;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.startRoll = startRoll;
        this.endRoll = endRoll;
    }

    public ClassObject() {
    }
}
