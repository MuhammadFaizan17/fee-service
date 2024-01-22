package com.rak.fee.validator;

public interface Validator <E,D>{
    void validateEntity(E e);
    void validateDTO(D d);
}
