package com.rak.fee.controller;

import com.rak.fee.dto.FeeDTO;
import com.rak.fee.enums.Grade;
import com.rak.fee.service.FeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/fee")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @GetMapping
    @Operation(summary = "Get All Configured Fee")
    public List<FeeDTO> getAllFees() {
        return feeService.getAllFees();
    }

    @PostMapping
    @Operation(summary = "Configure Fee", description = "configure fee against grade (G1 to G10)")
    public ResponseEntity<FeeDTO> configureFee(@Valid @RequestBody FeeDTO feeDTO) {
        feeService.configureFee(feeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping
    @Operation(summary = "Update Fee", description = "Update fee against grade (G1 to G10)")
    public ResponseEntity<FeeDTO> updateFee(@Valid @RequestBody FeeDTO feeDTO) {
        return feeService.updateFee(feeDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "record not found against grade: " + feeDTO.getGrade()));
    }


    @GetMapping("/grade")
    @Operation(summary = "Get fee", description = "Get fee by grade(G1 to G10)")
    public FeeDTO getFeeByGrade(@RequestParam Grade grade, @RequestParam Long schoolId) {
        return feeService.findByGradeAndSchoolId(grade.getGrade(), schoolId);

    }
}
