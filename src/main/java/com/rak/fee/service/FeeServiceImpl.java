package com.rak.fee.service;

import com.rak.fee.adapter.StudentAdapter;
import com.rak.fee.dto.FeeDTO;
import com.rak.fee.mapper.FeeMapper;
import com.rak.fee.repository.FeeRepository;
import com.rak.fee.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;
    private final FeeMapper feeMapper;
    @Qualifier("fee")
    private final Validator validator;

    @Override
    public List<FeeDTO> getAllFees() {
        return feeRepository.findAll().stream()
                .map(feeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FeeDTO> getFeeById(Long id) {
        return feeRepository.findById(id)
                .map(feeMapper::toDTO);
    }

    @Override
    public void createFee(FeeDTO feeDTO) {
        validator.validateDTO(feeDTO);
        feeRepository.findFirstByGradeAndSchoolId(feeDTO.getGrade(), feeDTO.getSchoolId()).ifPresentOrElse(x -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fee already exist against grade: " + feeDTO.getGrade());

        }, () -> {
            feeRepository.save(feeMapper.toEntity(feeDTO));
        });
    }

    @Override
    public Optional<FeeDTO> updateFee(FeeDTO feeDTO) {
        validator.validateDTO(feeDTO);
        return feeRepository.findFirstByGradeAndSchoolId(feeDTO.getGrade(), feeDTO.getSchoolId())
                .map(existingFee -> {
                    BeanUtils.copyProperties(feeDTO, existingFee, "id");
                    return feeMapper.toDTO(feeRepository.save(existingFee));
                });
    }

    @Override
    public boolean deleteFee(Long id) {
        if (feeRepository.existsById(id)) {
            feeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public FeeDTO findByGradeAndSchoolId(String grade, Long schoolId) {
        return feeMapper.toDTO(feeRepository.findFirstByGradeAndSchoolId(grade, schoolId).orElse(null));
    }
}

