package kopo.poly.dto;

import lombok.Builder;

@Builder
public record AnimalDTO(
        Long desertionNo,
        String fileName,
        Long happenDt,
        String happenPlace,
        String kindCd,
        String colorCd,
        String age,
        String weight,
        String noticeNo,
        long noticeSdt,
        long noticeEdt,
        String popfile,
        String processState,
        String sexCd,
        String neuterYn,
        String specialMark,
        String careNm,
        String careTel,
        String careAddr,
        String orgNm,
        String chargeNm,
        long officetel
) {
}
