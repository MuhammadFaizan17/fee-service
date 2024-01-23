package com.rak.fee.service;

import com.rak.fee.dto.FeeDTO;

import java.util.List;
import java.util.Optional;

public interface FeeService {

    List<FeeDTO> getAllFees();

    Optional<FeeDTO> getFeeById(Long id);

    void configureFee(FeeDTO feeDTO);

    Optional<FeeDTO> updateFee(FeeDTO feeDTO);

    boolean deleteFee(Long id);

    FeeDTO findByGradeAndSchoolId(String grade, Long schoolId);

}
