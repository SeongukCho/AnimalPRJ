package kopo.poly.service;

import feign.Param;
import feign.RequestLine;
import kopo.poly.dto.AnimalDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "FeignAnimalAPI", url = "http://apis.data.go.kr/1543061/abandonmentPublicSrvc")
public interface IFeignAnimalService {

    @RequestLine("GET /abandonmentPublic")
    AnimalDTO getAnimalList(
            @Param("bgnde") String bgnde,
            @Param("endde") String endde,
            @Param("upkind") String upkind,
            @Param("upr_cd") String upr_cd,
            @Param("org_cd") String org_cd,
            @Param("care_reg_no") String care_reg_no,
            @Param("state") String state,
            @Param("neuter_yn") String neuter_yn,
            @Param("pageNo") String pageNo,
            @Param("numOfRows") String numOfRows,
            @Param("_type") String type,
            @Param("serviceKey") String serviceKey
    );
}
