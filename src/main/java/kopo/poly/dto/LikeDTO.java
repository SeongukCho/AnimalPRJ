package kopo.poly.dto;

import lombok.Builder;

@Builder
public record LikeDTO(
        Long boardSeq,
        Long userSeq,
        String existsYn
) {
}