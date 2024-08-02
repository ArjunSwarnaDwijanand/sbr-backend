package com.dailycodework.sbrdemo.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CustomExceptionHandlerTest {

    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Test
    void testHandleException() {
        // Mock the BindingResult
        BindingResult bindingResult = mock(BindingResult.class);

        // Mock FieldErrors
        FieldError fieldError1 = new FieldError("objectName", "field1", "error message 1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "error message 2");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        // Mock MethodArgumentNotValidException
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Expected error map
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("field1", "error message 1");
        expectedErrors.put("field2", "error message 2");

        // Call the exception handler
        Map<String, String> response = customExceptionHandler.handleException(ex);

        // Verify the result
        assertEquals(expectedErrors, response);
    }

    @Test
    void testUserNotFound() {
        StudentNotFoundException ex = new StudentNotFoundException("Student not found");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("error", "Student not found");

        Map<String, String> response = customExceptionHandler.userNotFound(ex);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testStudentAlreadyExists() {
        StudentAlreadyExistsException ex = new StudentAlreadyExistsException("Student already exists");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("error", "Student already exists");

        Map<String, String> response = customExceptionHandler.userNotFound(ex);

        assertEquals(expectedResponse, response);
    }
}
