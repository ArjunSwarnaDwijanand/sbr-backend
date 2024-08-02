package com.dailycodework.sbrdemo.service;

import com.dailycodework.sbrdemo.exception.StudentAlreadyExistsException;
import com.dailycodework.sbrdemo.exception.StudentNotFoundException;
import com.dailycodework.sbrdemo.model.Student;
import com.dailycodework.sbrdemo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Simpson Alfred
 */

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService{

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
    @Override
    public Student addStudent(Student student) {
        if (studentAlreadyExists(student.getEmail())){
            throw  new StudentAlreadyExistsException(student.getEmail()+ " already exists!");
        }
        return studentRepository.save(student);
    }


    @Override
    public Student updateStudent(Student student, Long id) {

        return studentRepository.findById(id)
                .map(existingStudent -> updateExistingStudent(existingStudent, student))
                .orElseThrow(() -> new StudentNotFoundException("Sorry, this student could not be found"));
    }

    private Student updateExistingStudent(Student existingStudent, Student newStudentData) {
        existingStudent.setFirstName(newStudentData.getFirstName());
        existingStudent.setLastName(newStudentData.getLastName());
        existingStudent.setEmail(newStudentData.getEmail());
        existingStudent.setDepartment(newStudentData.getDepartment());
        return studentRepository.save(existingStudent);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Sorry, no student found with the Id :" +id));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)){
            throw new StudentNotFoundException("Sorry, student not found");
        }
        studentRepository.deleteById(id);
    }
    private boolean studentAlreadyExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }
}
