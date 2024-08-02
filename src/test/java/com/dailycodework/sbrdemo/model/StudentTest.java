package com.dailycodework.sbrdemo.model;


import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class StudentTest {

    @Test
    void studentTest(){

        BeanTester beanTester= new BeanTester();
        beanTester.testBean(Student.class);
    }
}
