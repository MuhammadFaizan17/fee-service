package com.rak.fee.service;

import com.rak.fee.dto.FeeDTO;
import com.rak.fee.mapper.FeeMapper;
import com.rak.fee.repository.FeeRepository;
import com.rak.fee.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;
    private final FeeMapper feeMapper;
    @Qualifier("fee")
    private final Validator validator;


    /**
     * It retrieves all the fees from the fee repository and maps them to FeeDTO objects.
     * The method returns a list of FeeDTO objects.
     */
    @Override
    public List<FeeDTO> getAllFees() {
        log.info("inside getAllFees()");
        return feeRepository.findAll().stream()
                .map(feeMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * It retrieves a fee record from the fee repository by its ID and maps it to a FeeDTO object.
     * If the fee record exists, it returns an Optional containing the FeeDTO object, otherwise it returns an empty Optional.
     */
    @Override
    public Optional<FeeDTO> getFeeById(Long id) {
        log.info("fetching feeById with id: {}", id);
        return feeRepository.findById(id)
                .map(feeMapper::toDTO);
    }

    /**
     * It creates a fee based on the provided FeeDTO object.
     * If a fee already exists for the given grade and school ID, it throws a ResponseStatusException with a BAD_REQUEST status and an appropriate message.
     * Otherwise, it saves the fee by converting the FeeDTO object to an entity using the feeMapper and feeRepository.
     */
    @Override
    public void configureFee(FeeDTO feeDTO) {
        log.info("inside createFee");

        validator.validateDTO(feeDTO);
        feeRepository.findFirstByGradeAndSchoolId(feeDTO.getGrade(), feeDTO.getSchoolId()).ifPresentOrElse(x -> {
            log.error("fee already exist against grade: {}", feeDTO.getGrade());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fee already exist against grade: " + feeDTO.getGrade());
        }, () -> {
            feeRepository.save(feeMapper.toEntity(feeDTO));
            log.info("fee created successfully");
        });
    }

    /**
     * It updates the fee information for a given FeeDTO object.
     * The method first validates the FeeDTO object using the validator.
     * It then retrieves the existing fee from the fee repository based on the grade and school id in the FeeDTO object.
     * If an existing fee is found, the properties of the FeeDTO object are copied to the existing fee object, excluding the id.
     * The updated fee object is then saved in the fee repository and mapped to a FeeDTO object using the feeMapper.
     * Finally, the method returns an Optional containing the updated FeeDTO object.
     */
    @Override
    public Optional<FeeDTO> updateFee(FeeDTO feeDTO) {
        validator.validateDTO(feeDTO);
        return feeRepository.findFirstByGradeAndSchoolId(feeDTO.getGrade(), feeDTO.getSchoolId())
                .map(existingFee -> {
                    BeanUtils.copyProperties(feeDTO, existingFee, "id");
                    return feeMapper.toDTO(feeRepository.save(existingFee));
                });
    }

    /**
     * It checks if the fee with the given ID exists in the fee repository.
     * If it exists, the fee is deleted from the repository and the method returns true.
     * Otherwise, it returns false.
     */
    @Override
    public boolean deleteFee(Long id) {
        if (feeRepository.existsById(id)) {
            feeRepository.deleteById(id);
            log.info("fee record deleted with id: {}", id);
            return true;
        }
        log.info("fee record not found id: {}", id);
        return false;
    }

    /**
     * It implements the findByGradeAndSchoolId method, which retrieves a FeeDTO object
     * based on the provided grade and school ID. It uses the feeRepository to query
     * the database for the first Fee entity that matches the given grade and school ID.
     * If no matching entity is found, it returns null. The feeMapper is then used to
     * convert the Fee entity to a FeeDTO object.
     */
    @Override
    public FeeDTO findByGradeAndSchoolId(String grade, Long schoolId) {
        log.info("inside findByGradeAndSchoolId() with grade: {} and schoolId: {}", grade, schoolId);
        return feeMapper.toDTO(feeRepository.findFirstByGradeAndSchoolId(grade, schoolId).orElse(null));
    }
}

