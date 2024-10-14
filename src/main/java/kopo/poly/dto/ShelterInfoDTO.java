package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ShelterInfoDTO(

        String uprCd,
        String orgCd,
        String type,

        @Id
        String id,
        String careNm,          // 동물보호센터명
        String orgNm,           // 관리기관명
        String divisionNm,      // 동물보호센터유형
        String saveTrgtAnimal,  // 구조대상동물
        String careAddr,        // 소재지 도로명주소
        String jibunAddr,       // 소재지 지번주소
        Long lat,               // 위도
        Long lng,               // 경도
        String dsignationDate,  // 동물보호센터 지정일자
        String weekOprStime,        // 평일 운영시작시각
        String weekOprEtime,        // 평일 운영종료시각
        String weekCellStime,       // 평일 분양시작시각
        String weekCellEtime,       // 평일 분양종료시각
        String weekendOprStime,     // 주말 운영시작시각
        String weekendOprEtime,     // 주말 운영종료시각
        String weekendCellStime,    // 주말 분양시작시각
        String weekendCellEtime,    // 주말 분양종료시각
        String closeDay,        // 휴무일
        Long vetPersonCnt,      // 수의사 인원수
        Long specsPersonCnt,    // 사양관리사 인원수
        Long medicalCnt,        // 진료실 수
        Long breedCnt,          // 사육실 수
        Long quarabtineCnt,     // 격리실 수
        Long feedCnt,           // 사료보관실 수
        Long transCarCnt,       // 구조운반용차량 보유대 수
        String careTel,         // 전화번호
        String dataStdDt        // 데이터기준일자
) {
}
