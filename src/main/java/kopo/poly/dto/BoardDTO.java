package kopo.poly.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record BoardDTO(
        Long boardSeq, // 기본키, 순번
        String title, // 제목
        String contents, // 글 내용
        String userId, // 작성자
        Long readCnt, // 조회수
        String regId, // 등록자 아이디
        String regDt, // 등록일
        String chgId, // 수정자 아이디
        String chgDt, // 수정일
        String userName, // 등록자명
        String profilePath, // 프로필 경로
        String imagePath, // 게시글 이미지 경로
        List<BoardImgDTO> images // 이미지 파일들
) {
}