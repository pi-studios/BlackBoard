package com.pistudiosofficial.myclass.objects;

public class HelloListObject {
    public UserObject userObject;
    public int hello;

    // hello = 0: sent request
    // hello = 1: received request
    // hello = 2: friend

    public HelloListObject() {
    }

    public HelloListObject(UserObject userObject, int hello) {
        this.userObject = userObject;
        this.hello = hello;
    }
}
