package com.dailycodework.sbrdemo.service;

import com.dailycodework.sbrdemo.exception.StudentAlreadyExistsException;
import com.dailycodework.sbrdemo.exception.StudentNotFoundException;
import com.dailycodework.sbrdemo.model.Student;
import com.dailycodework.sbrdemo.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student existingStudent;
    private Student updatedStudent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Doe");
        existingStudent.setEmail("john.doe@example.com");
        existingStudent.setDepartment("IT");

        updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setFirstName("Jane");
        updatedStudent.setLastName("Doe");
        updatedStudent.setEmail("jane.doe@example.com");
        updatedStudent.setDepartment("HR");
    }

    @Test
    void testGetStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(existingStudent));

        List<Student> students = studentService.getStudents();
        assertEquals(1, students.size());
        assertEquals(existingStudent, students.get(0));
    }

    @Test
    void testAddStudentSuccess() {
        when(studentRepository.findByEmail(existingStudent.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        Student result = studentService.addStudent(existingStudent);
        assertEquals(existingStudent, result);
    }

    @Test
    void testAddStudentAlreadyExists() {
        when(studentRepository.findByEmail(existingStudent.getEmail())).thenReturn(Optional.of(existingStudent));

        StudentAlreadyExistsException thrown = assertThrows(StudentAlreadyExistsException.class, () -> {
            studentService.addStudent(existingStudent);
        });

        assertEquals(existingStudent.getEmail() + " already exists!", thrown.getMessage());
    }

    @Test
    void testUpdateStudentSuccess() {
        // Arrange
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        Student result = studentService.updateStudent(updatedStudent, existingStudent.getId());

        // Assert
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("HR", result.getDepartment());
        verify(studentRepository).save(existingStudent);
    }

    @Test
    void testUpdateStudentNotFound() {
        // Arrange
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.empty());

        // Act & Assert
        StudentNotFoundException thrown = assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateStudent(updatedStudent, existingStudent.getId());
        });

        assertEquals("Sorry, this student could not be found", thrown.getMessage());
    }

    @Test
    void testGetStudentByIdSuccess() {
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.of(existingStudent));

        Student result = studentService.getStudentById(existingStudent.getId());
        assertEquals(existingStudent, result);
    }

    @Test
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.empty());

        StudentNotFoundException thrown = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(existingStudent.getId());
        });

        assertEquals("Sorry, no student found with the Id :" + existingStudent.getId(), thrown.getMessage());
    }

    @Test
    void testDeleteStudentSuccess() {
        when(studentRepository.existsById(existingStudent.getId())).thenReturn(true);
        doNothing().when(studentRepository).deleteById(existingStudent.getId());

        assertDoesNotThrow(() -> studentService.deleteStudent(existingStudent.getId()));
        verify(studentRepository, times(1)).deleteById(existingStudent.getId());
    }

    @Test
    void testDeleteStudentNotFound() {
        when(studentRepository.existsById(existingStudent.getId())).thenReturn(false);

        StudentNotFoundException thrown = assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudent(existingStudent.getId());
        });

        assertEquals("Sorry, student not found", thrown.getMessage());
    }
}
