package com.dailycodework.sbrdemo.controller;

import com.dailycodework.sbrdemo.model.Student;
import com.dailycodework.sbrdemo.service.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        student= Student.builder().firstName("Jonh").lastName("Doe").email("john.doe@example.com")
                .department("CES").build();
    }

    @Test
    void testGetStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentService.getStudents()).thenReturn(students);

        ResponseEntity<List<Student>> response = studentController.getStudents();
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(students, response.getBody());
    }
    @Test
    void testAddStudent() {
        when(studentService.addStudent(student)).thenReturn(student);

        Student createdStudent = studentController.addStudent(student);
        assertEquals(student, createdStudent);
    }

    @Test
    void testUpdateStudent() {
        when(studentService.updateStudent(student, 1L)).thenReturn(student);

        Student updatedStudent = studentController.updateStudent(student, 1L);
        assertEquals(student, updatedStudent);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentService).deleteStudent(1L);

        studentController.deleteStudent(1L);
        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void testGetStudentById() {
        when(studentService.getStudentById(1L)).thenReturn(student);

        Student foundStudent = studentController.getStudentById(1L);
        assertEquals(student, foundStudent);
    }

    // Additional tests for edge cases and error handling

    @Test
    void testGetStudentByIdNotFound() {
        when(studentService.getStudentById(1L)).thenReturn(null);

        Student foundStudent = studentController.getStudentById(1L);
        assertEquals(null, foundStudent);
    }

    @Test
    void testUpdateStudentNotFound() {
        when(studentService.updateStudent(student, 1L)).thenReturn(null);

        Student updatedStudent = studentController.updateStudent(student, 1L);
        assertEquals(null, updatedStudent);
    }

    @Test
    void testDeleteStudentNotFound() {
        doThrow(new IllegalArgumentException("Student not found")).when(studentService).deleteStudent(1L);

        try {
            studentController.deleteStudent(1L);
        } catch (IllegalArgumentException e) {
            assertEquals("Student not found", e.getMessage());
        }

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
