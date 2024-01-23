package com.rak.fee.service;


import com.rak.fee.dto.FeeDTO;
import com.rak.fee.entity.Fee;
import com.rak.fee.enums.Grade;
import com.rak.fee.mapper.FeeMapper;
import com.rak.fee.repository.FeeRepository;
import com.rak.fee.validator.FeeValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@Slf4j
public class FeeServiceTest {
    @Mock
    private FeeRepository feeRepository;

    @Mock
    private FeeMapper feeMapper;

    @Mock
    private FeeValidator feeValidator;

    @InjectMocks
    private FeeServiceImpl feeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {
        // Prepare mock data
        List<Fee> mockFees = new ArrayList<>();
        mockFees.add(new Fee(1L, Grade.G1.getGrade(), 10.0, 1L));
        mockFees.add(new Fee(2L, Grade.G2.getGrade(), 5.0, 1L));
        List<FeeDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new FeeDTO(1L, Grade.G1.getGrade(), 10.0, 1L));
        expectedDTOs.add(new FeeDTO(2L, Grade.G2.getGrade(), 5.0, 1L));


        // Mock repository method
        when(feeRepository.findAll()).thenReturn(mockFees);

        // Mock mapper method
        when(feeMapper.toDTO(mockFees.get(0))).thenReturn(expectedDTOs.get(0));
        when(feeMapper.toDTO(mockFees.get(1))).thenReturn(expectedDTOs.get(1));

        // Call the service method
        List<FeeDTO> actualDTOs = feeService.getAllFees();

        // Verify the result
        assertEquals(expectedDTOs, actualDTOs);
    }

    @Test
    public void getEmptyList() {
        // Prepare mock data
        List<Fee> mockFees = new ArrayList<>();

        // Mock repository method
        when(feeRepository.findAll()).thenReturn(mockFees);

        // Call the service method
        List<FeeDTO> actualDTOs = feeService.getAllFees();

        // Verify the result
        assertEquals(0, actualDTOs.size());
    }

    @Test
    public void getFeeById() {
        // Mock data
        Long feeId = 1L;
        Fee feeEntity = new Fee(1L, "G1", 10.0, 1L);
        FeeDTO expectedDTO = new FeeDTO(1L, "G1", 10.0, 1L);

        // Mocking behavior
        when(feeRepository.findById(feeId)).thenReturn(Optional.of(feeEntity));
        when(feeMapper.toDTO(feeEntity)).thenReturn(expectedDTO);

        // Method invocation
        Optional<FeeDTO> result = feeService.getFeeById(feeId);

        // Verification
        verify(feeRepository).findById(feeId);
        verify(feeMapper).toDTO(feeEntity);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(expectedDTO, result.get());
    }

    @Test
    public void getFeeByIdNotFound() {
        // Mock data
        Long feeId = 1L;

        // Mocking behavior
        when(feeRepository.findById(feeId)).thenReturn(Optional.empty());

        // Method invocation
        Optional<FeeDTO> result = feeService.getFeeById(feeId);

        // Verification
        verify(feeRepository).findById(feeId);
        verify(feeMapper, never()).toDTO(any());

        // Assertions
        assertFalse(result.isPresent());
    }

    @Test
    public void createFee() {
        FeeDTO feeDTO = new FeeDTO(null, "G1", 10.0, 1L);
        Fee feeEntity = new Fee(null, "G1", 10.0, 1L);

        // Mocking behavior
        Mockito.doNothing().when(feeValidator).validateDTO(feeDTO);
        when(feeRepository.findFirstByGradeAndSchoolId("G1", 1L)).thenReturn(Optional.empty());
        when(feeMapper.toEntity(feeDTO)).thenReturn(feeEntity);

        // Method invocation
        feeService.configureFee(feeDTO);

        // Verification
        verify(feeValidator).validateDTO(feeDTO);
        verify(feeRepository).findFirstByGradeAndSchoolId("G1", 1L);
        verify(feeMapper).toEntity(feeDTO);
        verify(feeRepository).save(feeEntity);
        // Verify the repository method is called
        verify(feeRepository, times(1)).save(any(Fee.class));
    }

    @Test
    public void updateFee() {
        // Mock data
        FeeDTO feeDTO = new FeeDTO(1L, "G1", 10.0, 1L);
        Fee existingFee = new Fee(1L, "G1", 5.0, 1L);
        Fee updatedFeeEntity = new Fee(1L, "G1", 10.0, 1L);

        // Mocking behavior

        when(feeRepository.findFirstByGradeAndSchoolId("G1", 1L)).thenReturn(Optional.of(existingFee));
        when(feeRepository.save(existingFee)).thenReturn(updatedFeeEntity);
        when(feeMapper.toDTO(updatedFeeEntity)).thenReturn(feeDTO);

        // Method invocation
        Optional<FeeDTO> result = feeService.updateFee(feeDTO);

        // Verification
        verify(feeValidator).validateDTO(feeDTO);
        verify(feeRepository).findFirstByGradeAndSchoolId("G1", 1L);
        verify(feeRepository).save(existingFee);
        verify(feeMapper).toDTO(updatedFeeEntity);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(feeDTO, result.get());
    }

    @Test
    public void updateNonExistingFee() {
        // Mock data
        FeeDTO feeDTO = new FeeDTO(1L, "G1", 10.0, 1L);

        // Mocking behavior
//        Mockito.doThrow(ResponseStatusException.class).when(feeValidator).validateDTO(feeDTO);

        when(feeRepository.findFirstByGradeAndSchoolId("G1", 1L)).thenReturn(Optional.empty());

        // Method invocation
        Optional<FeeDTO> result = feeService.updateFee(feeDTO);

        // Verification
        verify(feeValidator).validateDTO(feeDTO);
        verify(feeRepository).findFirstByGradeAndSchoolId("G1", 1L);
        verify(feeRepository, never()).save(any());

        // Assertions
        assertFalse(result.isPresent());
    }

    @Test
    public void delete() {
        // Mocking repository behavior
        when(feeRepository.existsById(1L)).thenReturn(true);

        // Call the service method
        boolean isDeleted = feeService.deleteFee(1L);

        // Verify the repository method is called
        verify(feeRepository, times(1)).deleteById(1L);

        // Assert the result
        assertTrue(isDeleted);
    }
}
