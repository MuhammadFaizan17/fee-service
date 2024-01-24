package com.rak.fee.controller;


import com.rak.fee.dto.FeeDTO;
import com.rak.fee.enums.Grade;
import com.rak.fee.service.FeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class FeeControllerTest {
    @Mock
    private FeeService feeService;

    @InjectMocks
    private FeeController feeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllFees() {

        List<FeeDTO> expectedFees = new ArrayList<>();
        expectedFees.add(new FeeDTO());
        expectedFees.add(new FeeDTO());
        when(feeService.getAllFees()).thenReturn(expectedFees);


        List<FeeDTO> actualFees = feeController.getAllFees();


        assertNotNull(actualFees);
        assertEquals(expectedFees.size(), actualFees.size());
    }

    @Test
    void createFee() {

        FeeDTO feeDTO = new FeeDTO();


        ResponseEntity<FeeDTO> responseEntity = feeController.configureFee(feeDTO);


        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void updateFee() {

        FeeDTO feeDTO = new FeeDTO();
        when(feeService.updateFee(feeDTO)).thenReturn(Optional.of(feeDTO));


        ResponseEntity<FeeDTO> responseEntity = feeController.updateFee(feeDTO);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(feeDTO, responseEntity.getBody());
    }

    @Test
    void updateFeeShouldThrowExceptionWhenFeeNotFound() {

        FeeDTO feeDTO = new FeeDTO();
        when(feeService.updateFee(feeDTO)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> feeController.updateFee(feeDTO));
    }

    @Test
    void getFeeByGradeAndSchoolId() {

        Grade grade = Grade.G1;
        Long schoolId = 1L;
        FeeDTO expectedFee = new FeeDTO();
        when(feeService.findByGradeAndSchoolId(grade.getGrade(), schoolId)).thenReturn(expectedFee);


        FeeDTO actualFee = feeController.getFeeByGrade(grade, schoolId);


        assertNotNull(actualFee);
        assertEquals(expectedFee, actualFee);
    }
}
