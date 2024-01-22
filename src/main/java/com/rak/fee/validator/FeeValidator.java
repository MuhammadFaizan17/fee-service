package com.rak.fee.validator;

import com.rak.fee.adapter.StudentAdapter;
import com.rak.fee.dto.FeeDTO;
import com.rak.fee.entity.Fee;
import com.rak.fee.enums.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Qualifier("fee")
@RequiredArgsConstructor
public class FeeValidator implements Validator<Fee, FeeDTO> {

    private final StudentAdapter studentAdapter;

    @Override
    public void validateEntity(Fee fee) {

    }

    @Override
    public void validateDTO(FeeDTO feeDTO) {
        if (!Grade.isValid(feeDTO.getGrade()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid grade value must be like G1 to G10");
        if (feeDTO.getAmount() < 10)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be greater than or equal to 10 AED");
        if (studentAdapter.getSchoolById(feeDTO.getSchoolId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "school not found with schoolId: " + feeDTO.getSchoolId());
        }

    }
}
