package com.example.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String email;
    private String program;
    private int semester;
    
    public Student() {
        this.name = "John Doe";
        this.email = "john.doe@example.com";
        this.program = "Computer Science";
        this.semester = 5;
    }
    
    public Student(String name, String email, String program, int semester) {
        this.name = name;
        this.email = email;
        this.program = program;
        this.semester = semester;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}
