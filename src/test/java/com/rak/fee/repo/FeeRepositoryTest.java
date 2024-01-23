package com.rak.fee.repo;

import com.rak.fee.entity.Fee;
import com.rak.fee.repository.FeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class FeeRepositoryTest {

    @Autowired
    private FeeRepository feeRepository;

    @Test
    public void findFirstByGradeAndSchoolIdWithValidInputs() {

        String grade = "G1";
        Long schoolId = 1L;
        Fee fee = new Fee(1L, grade, 10.0, schoolId);

        feeRepository.save(fee);


        Optional<Fee> result = feeRepository.findFirstByGradeAndSchoolId(grade, schoolId);


        assertTrue(result.isPresent());
        assertEquals(fee.getId(), result.get().getId());
        assertEquals(fee.getAmount(), result.get().getAmount());
        assertEquals(fee.getGrade(), result.get().getGrade());
        assertEquals(fee.getSchoolId(), result.get().getSchoolId());


    }

    @Test
    public void findFirstByGradeAndSchoolIdWithInvalidGrade() {

        String grade = "G1";
        Long schoolId = 1L;
        Fee fee = new Fee(1L, grade, 10.0, schoolId);

        feeRepository.save(fee);


        Optional<Fee> result = feeRepository.findFirstByGradeAndSchoolId("G2", schoolId);


        assertFalse(result.isPresent());
    }

    @Test
    public void findFirstByGradeAndSchoolIdWithInvalidSchoolId() {

        String grade = "G1";
        Long schoolId = 5L;
        Fee fee = new Fee(1L, grade, 10.0, schoolId);

        feeRepository.save(fee);


        Optional<Fee> result = feeRepository.findFirstByGradeAndSchoolId(grade, 8L);


        assertFalse(result.isPresent());
    }
}