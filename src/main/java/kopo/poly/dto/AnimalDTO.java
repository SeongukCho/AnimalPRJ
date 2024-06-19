package kopo.poly.dto;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Document
public record AnimalDTO(

        /* 요청 항목 */
        String bgnde,       // 검색 시작일
        String endde,       // 검색 종료일
        String upkind,        // 축종 코드 (개 : 417000, 고양이 : 422400, 기타 : 429900)
        String kind,          // 품종 코드
        String uprCd,        // 시도 코드
        String orgCd,        // 시군구 코드
        String careRegNo,   // 보호소 번호
        String state,         // 상태(전체 : null(빈값), 공고중 : notice, 보호중 : protect)
        String neuterYnR,    // 상태 (전체 : null(빈값), 예 : Y, 아니오 : N, 미상 : U)

        /* 응답 항목 */
        @Id
        Long desertionNo,     // 유기번호
        String fileName,      // 썸네일 이미지
        String happenDt,        // 접수일
        String happenPlace,   // 발견 장소
        String kindCd,        // 품종
        String colorCd,       // 생상
        String age,           // 나이
        String weight,        // 체중
        String noticeNo,      // 공고 번호
        String noticeSdt,       // 공고 시작일
        String noticeEdt,       // 공고 종료일
        String popfile,         // 이미지
        String processState,    // 상태
        String sexCd,           // 성별
        String neuterYn,        // 중성화 여부
        String specialMark,     // 특징
        String careNm,          // 보호소 이름
        String careTel,         // 보호소 전화번호
        String careAddr,        // 보호 장소
        String orgNm,           // 관할 기관
        String chargeNm,        // 담당자
        Long officetel,         // 담당자 연락처
        String noticeComment    // 특이사항
) {
}
