package kopo.poly.service;

import feign.Param;
import feign.RequestLine;
import kopo.poly.dto.*;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "FeignAnimalAPI", url = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc")
public interface IAnimalAPIService {


    @RequestLine("GET /sido?numOfRows={numOfRows}&pageNo={pageNo}&%5Ftype={type}&serviceKey={serviceKey}")
    SidoDTO getSidoList(
            @Param("numOfRows") int numOfRows,
            @Param("pageNo") int pageNo,
            @Param("type") String type,
            @Param("serviceKey") String serviceKey
            );

    @RequestLine("GET /sigungu?upr_cd={uprCd}&%5Ftype={type}&serviceKey={serviceKey}")
    SigunguDTO getSigunguList(
            @Param("upr_cd") String uprCd,
            @Param("type") String type,
            @Param("serviceKey") String serviceKey
    );

    @RequestLine("GET /shelter")
    ShelterDTO getShelterList(
            @Param("upr_cd") String uprCd,
            @Param("org_cd") String orgCd,
            @Param("type") String type,
            @Param("serviceKey") String serviceKey
    );

    @RequestLine("GET /kind")
    KindDTO getKindList(
            @Param("up_kind_cd") String upKindCd,
            @Param("type") String type,
            @Param("serviceKey") String serviceKey
    );

    @RequestLine("GET /abandonmentPublic")
    AnimalDTO getAnimalList(
            @Param("bgnde") String bgnde,
            @Param("endde") String endde,
            @Param("upkind") String upkind,
            @Param("kind") String kind,
            @Param("upr_cd") String upr_cd,
            @Param("org_cd") String org_cd,
            @Param("care_reg_no") String care_reg_no,
            @Param("state") String state,
            @Param("neuter_yn") String neuter_yn,
            @Param("pageNo") String pageNo,
            @Param("numOfRows") String numOfRows,
            @Param("type") String type,
            @Param("serviceKey") String serviceKey
    );
}
