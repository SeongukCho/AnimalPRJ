package kopo.poly.dto;

import lombok.Builder;

@Builder
public record BoardImgDTO(
        Long imageSeq,
        Long boardSeq,
        String imagePath

) {

}