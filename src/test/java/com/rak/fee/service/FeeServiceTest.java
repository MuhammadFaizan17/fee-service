package com.rak.fee.service;


import com.rak.fee.dto.FeeDTO;
import com.rak.fee.entity.Fee;
import com.rak.fee.enums.Grade;
import com.rak.fee.mapper.FeeMapper;
import com.rak.fee.repository.FeeRepository;
import com.rak.fee.validator.FeeValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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


//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest()
//@AutoConfigureMockMvc(addFilters = false)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

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
        List<Fee> mockFees = new ArrayList<>();
        mockFees.add(new Fee(1L, Grade.G1.getGrade(), 10.0, 1L));
        mockFees.add(new Fee(2L, Grade.G2.getGrade(), 5.0, 1L));
        List<FeeDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new FeeDTO(1L, Grade.G1.getGrade(), 10.0, 1L));
        expectedDTOs.add(new FeeDTO(2L, Grade.G2.getGrade(), 5.0, 1L));


        when(feeRepository.findAll()).thenReturn(mockFees);

        when(feeMapper.toDTO(mockFees.get(0))).thenReturn(expectedDTOs.get(0));
        when(feeMapper.toDTO(mockFees.get(1))).thenReturn(expectedDTOs.get(1));

        List<FeeDTO> actualDTOs = feeService.getAllFees();

        assertEquals(expectedDTOs, actualDTOs);
    }

    @Test
    public void getEmptyList() {
        List<Fee> mockFees = new ArrayList<>();
        when(feeRepository.findAll()).thenReturn(mockFees);
        List<FeeDTO> actualDTOs = feeService.getAllFees();
        assertEquals(0, actualDTOs.size());
    }

    @Test
    public void getFeeById() {
        Long feeId = 1L;
        Fee feeEntity = new Fee(1L, "G1", 10.0, 1L);
        FeeDTO expectedDTO = new FeeDTO(1L, "G1", 10.0, 1L);
        when(feeRepository.findById(feeId)).thenReturn(Optional.of(feeEntity));
        when(feeMapper.toDTO(feeEntity)).thenReturn(expectedDTO);
        Optional<FeeDTO> result = feeService.getFeeById(feeId);
        verify(feeRepository).findById(feeId);
        verify(feeMapper).toDTO(feeEntity);
        assertTrue(result.isPresent());
        assertEquals(expectedDTO, result.get());
    }

    @Test
    public void getFeeByIdNotFound() {
        Long feeId = 1L;
        when(feeRepository.findById(feeId)).thenReturn(Optional.empty());
        Optional<FeeDTO> result = feeService.getFeeById(feeId);
        verify(feeRepository).findById(feeId);
        verify(feeMapper, never()).toDTO(any());
        assertFalse(result.isPresent());
    }

    @Test
    public void createFee() {
        FeeDTO feeDTO = new FeeDTO(null, "G1", 10.0, 1L);
        Fee feeEntity = new Fee(null, "G1", 10.0, 1L);
        Mockito.doNothing().when(feeValidator).validateDTO(feeDTO);

        when(feeRepository.findFirstByGradeAndSchoolId("G1", 1L)).thenReturn(Optional.empty());
        when(feeMapper.toEntity(feeDTO)).thenReturn(feeEntity);

        feeService.configureFee(feeDTO);

        verify(feeValidator).validateDTO(feeDTO);
        verify(feeRepository).findFirstByGradeAndSchoolId("G1", 1L);
        verify(feeMapper).toEntity(feeDTO);
        verify(feeRepository).save(feeEntity);
        verify(feeRepository, times(1)).save(any(Fee.class));
    }

    @Test
    public void updateFee() {
        FeeDTO feeDTO = new FeeDTO(1L, "G1", 10.0, 1L);
        Fee existingFee = new Fee(1L, "G1", 5.0, 1L);
        Fee updatedFeeEntity = new Fee(1L, "G1", 10.0, 1L);


        when(feeRepository.findFirstByGradeAndSchoolId("G1", 1L)).thenReturn(Optional.of(existingFee));
        when(feeRepository.save(existingFee)).thenReturn(updatedFeeEntity);
        when(feeMapper.toDTO(updatedFeeEntity)).thenReturn(feeDTO);

        Optional<FeeDTO> result = feeService.updateFee(feeDTO);

        verify(feeValidator).validateDTO(feeDTO);
        verify(feeRepository).findFirstByGradeAndSchoolId("G1", 1L);
        verify(feeRepository).save(existingFee);
        verify(feeMapper).toDTO(updatedFeeEntity);

        assertTrue(result.isPresent());
        assertEquals(feeDTO, result.get());
    }

    @Test
    public void updateNonExistingFee() {
        FeeDTO feeDTO = new FeeDTO(1L, "G1", 10.0, 1L);

        when(feeRepository.findFirstByGradeAndSchoolId("G1", 1L)).thenReturn(Optional.empty());

        Optional<FeeDTO> result = feeService.updateFee(feeDTO);

        verify(feeValidator).validateDTO(feeDTO);
        verify(feeRepository).findFirstByGradeAndSchoolId("G1", 1L);
        verify(feeRepository, never()).save(any());

        assertFalse(result.isPresent());
    }

    @Test
    public void delete() {
        when(feeRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = feeService.deleteFee(1L);

        verify(feeRepository, times(1)).deleteById(1L);

        assertTrue(isDeleted);
    }
}
