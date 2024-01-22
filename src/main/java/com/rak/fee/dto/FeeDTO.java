package com.rak.fee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeeDTO implements Serializable {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotBlank(message = "grade is required")
    private String grade;

    @NotNull
    @Min(value = 10, message = "amount must be greater than or equal to 10 AED")
    private Double amount;

    @NotNull
    private Long schoolId;
}
