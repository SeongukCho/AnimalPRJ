package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverDTO(

        ResponseDTO response,
        LinkedHashMap<String, Object> additionalProperties

) {

    @Data
    public static class ResponseDTO {
        private String id;
        private String name;
        private String nickname;
        private String email;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}