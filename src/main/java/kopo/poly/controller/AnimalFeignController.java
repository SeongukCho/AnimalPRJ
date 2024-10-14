package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.SidoDTO;
import kopo.poly.dto.SigunguDTO;
import kopo.poly.service.IAnimalFeignService;
import kopo.poly.service.IWeatherService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequestMapping(value = "api")
@RequiredArgsConstructor
@RestController
public class AnimalFeignController {

    private final IAnimalFeignService animalFeignService;
    private final IWeatherService weatherService;

    @Value("${api.decodingKey}")
    private String serviceKey;

    @Value("${api.type}")
    private String type;

    @GetMapping(value = "sido")
    public SidoDTO getSidoList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getSidoList Controller Start!");

        SidoDTO pDTO = SidoDTO.builder().numOfRows(17).pageNo(1).type(type).serviceKey(serviceKey).build();

        SidoDTO rDTO = Optional.ofNullable(animalFeignService.getSidoList(pDTO))
                .orElseGet(() -> SidoDTO.builder().build());

        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".getSidoList Controller End!");

        return rDTO;
    }

    @GetMapping(value = "sigungu")
    public SigunguDTO getSigunguList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getSigunguList Controller Start!");

        String uprCd = CmmUtil.nvl(request.getParameter("orgCd"));

        SigunguDTO pDTO = SigunguDTO.builder().uprCd(uprCd).type(type).serviceKey(serviceKey).build();

        SigunguDTO rDTO = Optional.ofNullable(animalFeignService.getSigunguList(pDTO))
                .orElseGet(() -> SigunguDTO.builder().build());

        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".getSigunguList Controller End!");

        return rDTO;
    }
}
