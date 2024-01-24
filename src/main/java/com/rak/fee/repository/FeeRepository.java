package com.rak.fee.repository;

import com.rak.fee.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findFirstByGradeAndSchoolId(String grade, Long schoolId);
}

