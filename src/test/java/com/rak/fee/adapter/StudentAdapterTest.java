package com.rak.fee.adapter;


import com.rak.fee.dto.SchoolDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StudentAdapterTest {


    @Mock
    private RestTemplate restTemplate;

    private StudentAdapter studentAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentAdapter = new StudentAdapter(restTemplate);
    }

    @Test
    void getSchoolByIdPositiveCase() {
        // Arrange
        Long schoolId = 1L;
        SchoolDTO expectedSchool = new SchoolDTO();
        ResponseEntity<SchoolDTO> responseEntity = new ResponseEntity<>(expectedSchool, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(SchoolDTO.class))).thenReturn(responseEntity);

        // Act
        SchoolDTO actualSchool = studentAdapter.getSchoolById(schoolId);

        // Assert
        assertEquals(expectedSchool, actualSchool);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(SchoolDTO.class));
    }

    @Test
    void getSchoolByIdRestClientException() {
        // Arrange
        Long schoolId = 1L;
        when(restTemplate.getForEntity(anyString(), eq(SchoolDTO.class))).thenThrow(new RestClientException("Error"));

        // Act and Assert
        assertThrows(RestClientException.class, () -> studentAdapter.getSchoolById(schoolId));
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(SchoolDTO.class));
    }
}

