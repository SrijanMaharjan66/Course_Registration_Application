package com.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Student student;
    
    public User() {}
    
    public User(String username, String password, Student student) {
        this.username = username;
        this.password = password;
        this.student = student;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
