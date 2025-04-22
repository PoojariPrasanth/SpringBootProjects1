package com.example.jwtAuthentication.controller;

import com.example.jwtAuthentication.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> students=new ArrayList<Student>(List.of(
            new Student(1,"Navin",60),
            new Student(2,"Kiran",65)));



    @GetMapping("students")
    public List<Student> getAllStudent(){
        return students;
    }

    @PostMapping("students")
    public Student addStudent(@RequestBody Student student){
        students.add(student);
        return student;
    }


}
