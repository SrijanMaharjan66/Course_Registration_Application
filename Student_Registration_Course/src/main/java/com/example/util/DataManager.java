package com.example.util;

import com.example.model.Course;
import com.example.model.Student;
import com.example.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final String STUDENT_FILE = "student_data.txt";
    private static final String COURSES_FILE = "registered_courses.txt";
    private static final String USERS_FILE = "users_data.txt";
    
    private static Student currentStudent = new Student();
    private static List<Course> registeredCourses = new ArrayList<>();
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    
    // Available courses for each program
    private static final List<Course> availableCourses = new ArrayList<>();
    
    static {
        initializeAvailableCourses();
        loadData();
    }
    
    private static void initializeAvailableCourses() {
        // BBA Courses (4 subjects)
        availableCourses.add(new Course("BBA101", "Principles of Management", "BBA", 3));
        availableCourses.add(new Course("BBA102", "Business Mathematics", "BBA", 3));
        availableCourses.add(new Course("BBA103", "Financial Accounting", "BBA", 4));
        availableCourses.add(new Course("BBA104", "Business Communication", "BBA", 2));
        
        // BBS Courses (4 subjects)
        availableCourses.add(new Course("BBS201", "Business Statistics", "BBS", 3));
        availableCourses.add(new Course("BBS202", "Microeconomics", "BBS", 3));
        availableCourses.add(new Course("BBS203", "Business Law", "BBS", 3));
        availableCourses.add(new Course("BBS204", "Organizational Behavior", "BBS", 3));
        
        // BCS Courses (4 subjects)
        availableCourses.add(new Course("BCS301", "Data Structures and Algorithms", "BCS", 4));
        availableCourses.add(new Course("BCS302", "Database Management Systems", "BCS", 4));
        availableCourses.add(new Course("BCS303", "Object Oriented Programming", "BCS", 4));
        availableCourses.add(new Course("BCS304", "Computer Networks", "BCS", 3));
    }
    
    public static Student getCurrentStudent() {
        return currentStudent;
    }
    
    public static void setCurrentStudent(Student student) {
        currentStudent = student;
        saveStudentData();
    }
    
    public static List<Course> getAvailableCourses() {
        return new ArrayList<>(availableCourses);
    }
    
    public static List<Course> getRegisteredCourses() {
        return new ArrayList<>(registeredCourses);
    }
    
    public static void registerCourse(Course course) {
        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
            saveRegisteredCourses();
        }
    }
    
    public static void unregisterCourse(Course course) {
        registeredCourses.remove(course);
        saveRegisteredCourses();
    }
    
    public static boolean isRegistered(Course course) {
        return registeredCourses.contains(course);
    }
    
    private static void saveStudentData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            writer.println("# Student Data File");
            writer.println("# Format: name|email|program|semester");
            writer.println(currentStudent.getName() + "|" + 
                          currentStudent.getEmail() + "|" + 
                          currentStudent.getProgram() + "|" + 
                          currentStudent.getSemester());
        } catch (IOException e) {
            System.err.println("Error saving student data: " + e.getMessage());
        }
    }
    
    private static void saveRegisteredCourses() {
        saveUserCourses();
        // Also save to old file for backward compatibility
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSES_FILE))) {
            writer.println("# Registered Courses File");
            writer.println("# Format: courseCode|courseName|program|credits");
            for (Course course : registeredCourses) {
                writer.println(course.getCourseCode() + "|" + 
                              course.getCourseName() + "|" + 
                              course.getProgram() + "|" + 
                              course.getCredits());
            }
        } catch (IOException e) {
            System.err.println("Error saving registered courses: " + e.getMessage());
        }
    }
    
    private static void loadData() {
        loadUsersData();
        
        // Load student data (for backward compatibility)
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue; // Skip comments and empty lines
                }
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    currentStudent = new Student(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            currentStudent = new Student();
        }
        
        // Load registered courses (for backward compatibility)
        try (BufferedReader reader = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            registeredCourses.clear();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue; // Skip comments and empty lines
                }
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    Course course = new Course(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                    registeredCourses.add(course);
                }
            }
        } catch (IOException | NumberFormatException e) {
            registeredCourses = new ArrayList<>();
        }
    }

    // User management methods
    public static boolean registerUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        saveUsersData();
        return true;
    }

    public static boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            currentStudent = user.getStudent();
            loadUserCourses(username);
            return true;
        }
        return false;
    }

    public static boolean userExists(String username) {
        return users.containsKey(username);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
        currentStudent = new Student();
        registeredCourses.clear();
    }

    private static void saveUsersData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("# Users Data File");
            writer.println("# Format: username|password|name|email|program|semester");
            for (User user : users.values()) {
                Student student = user.getStudent();
                writer.println(user.getUsername() + "|" + 
                              user.getPassword() + "|" + 
                              student.getName() + "|" + 
                              student.getEmail() + "|" + 
                              student.getProgram() + "|" + 
                              student.getSemester());
            }
        } catch (IOException e) {
            System.err.println("Error saving users data: " + e.getMessage());
        }
    }

    private static void loadUsersData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            users.clear();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue; // Skip comments and empty lines
                }
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Student student = new Student(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
                    User user = new User(parts[0], parts[1], student);
                    users.put(parts[0], user);
                }
            }
        } catch (IOException | NumberFormatException e) {
            // File doesn't exist or error reading, create default admin user
            users = new HashMap<>();
            Student defaultStudent = new Student("Admin User", "admin@example.com", "Computer Science", 5);
            User defaultUser = new User("student", "password123", defaultStudent);
            users.put("student", defaultUser);
            saveUsersData();
        }
    }

    private static void loadUserCourses(String username) {
        String userCoursesFile = "courses_" + username + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(userCoursesFile))) {
            String line;
            registeredCourses.clear();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue; // Skip comments and empty lines
                }
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    Course course = new Course(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                    registeredCourses.add(course);
                }
            }
        } catch (IOException | NumberFormatException e) {
            registeredCourses = new ArrayList<>();
        }
    }

    private static void saveUserCourses() {
        if (currentUser != null) {
            String userCoursesFile = "courses_" + currentUser.getUsername() + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(userCoursesFile))) {
                writer.println("# User Courses File for: " + currentUser.getUsername());
                writer.println("# Format: courseCode|courseName|program|credits");
                for (Course course : registeredCourses) {
                    writer.println(course.getCourseCode() + "|" + 
                                  course.getCourseName() + "|" + 
                                  course.getProgram() + "|" + 
                                  course.getCredits());
                }
            } catch (IOException e) {
                System.err.println("Error saving user courses: " + e.getMessage());
            }
        }
    }
    
    // Utility method to get all users (for admin purposes)
    public static Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }
    
    // Method to backup data
    public static void backupData() {
        try {
            // Create backup directory if it doesn't exist
            File backupDir = new File("backup");
            if (!backupDir.exists()) {
                backupDir.mkdir();
            }
            
            // Backup users data
            try (PrintWriter writer = new PrintWriter(new FileWriter("backup/users_backup.txt"))) {
                writer.println("# Users Backup - " + new java.util.Date());
                writer.println("# Format: username|password|name|email|program|semester");
                for (User user : users.values()) {
                    Student student = user.getStudent();
                    writer.println(user.getUsername() + "|" + 
                                  user.getPassword() + "|" + 
                                  student.getName() + "|" + 
                                  student.getEmail() + "|" + 
                                  student.getProgram() + "|" + 
                                  student.getSemester());
                }
            }
            
            System.out.println("Data backup completed successfully!");
            
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }
}
