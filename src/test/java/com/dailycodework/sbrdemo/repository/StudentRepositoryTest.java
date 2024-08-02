package com.dailycodework.sbrdemo.repository;


import com.dailycodework.sbrdemo.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.function.BiConsumer;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student savedStudent;

    @BeforeEach
    public void setUp() {
        // Clean up the database before each test
        studentRepository.deleteAll();
        // Arrange
        Student student = Student.builder().firstName("Arjun").lastName("Nag").email("john.doe@example.com")
                .department("CSE").build();

        //Act
        savedStudent =   studentRepository.save(student);
    }

    @Test
   void saveStudent(){

       // Arrange
      Student student1 = Student.builder().firstName("Arjun").lastName("Nag").email("john.doe@example.com")
               .department("CSE").build();


//      //Assert
        Assertions.assertThat(student1).isNotNull();


    }

    @Test
    void findByEmailTest(){

        // Retrieve student by email
        Optional<Student> studentOpt = studentRepository.findByEmail("john.doe@example.com");


        // Verify that the student was found and the email matches
        Assertions.assertThat(studentOpt).isPresent();
       Assertions.assertThat(studentOpt.get().getEmail()).isEqualTo("john.doe@example.com");

    }

    @Test
    void testFindByEmail_notFound() {
        // Attempt to retrieve a student with an email that doesn't exist
        Optional<Student> studentOpt = studentRepository.findByEmail("not.exist@example.com");

        // Verify that the student was not found
        Assertions.assertThat(studentOpt).isNotPresent();
    }



}
