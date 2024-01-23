package com.rak.fee.adapter;

import com.rak.fee.dto.SchoolDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
public class StudentAdapter {

    private final RestTemplate restTemplate;

    @Value("${student.service.url}")
    private String studentServiceUrl;

   /**
 * Retrieves a SchoolDTO object by its ID from the student service.
 * @param schoolId the ID of the school to retrieve
 * @return the SchoolDTO object retrieved from the student service
 * @throws RestClientException if an error occurs while retrieving the school
 */
public SchoolDTO getSchoolById(Long schoolId) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(studentServiceUrl + "/schools/" + schoolId);
    try {
        log.info("calling student service:{}", builder.toUriString());
        ResponseEntity<SchoolDTO> responseEntity = restTemplate.getForEntity(builder.toUriString(), SchoolDTO.class);
        return responseEntity.getBody();
    } catch (RestClientException e) {
        log.error("Error occurred while saving user on admin: {}", e.getMessage());
        throw (e);
    }
}

}
