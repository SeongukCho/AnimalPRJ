package kopo.poly.dto;

import lombok.Builder;

@Builder
public record ShelterDTO(
        long careRegNo,
        String careNm
) {
}
