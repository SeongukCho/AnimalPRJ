package kopo.poly.service.impl;

import kopo.poly.dto.*;
import kopo.poly.service.IAnimalAPIService;
import kopo.poly.service.IAnimalFeignService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimalFeignService implements IAnimalFeignService {

    private final IAnimalAPIService animalAPIService;

    @Override
    public SidoDTO getSidoList(SidoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getSidoList start!");

        String serviceKey = CmmUtil.nvl(pDTO.serviceKey());
        int numOfRows = pDTO.numOfRows();
        int pageNo = pDTO.pageNo();
        String type = CmmUtil.nvl(pDTO.type());

        log.info("serviceKey : " + serviceKey);
        log.info("numOfRows : " + numOfRows);
        log.info("pageNo : " + pageNo);
        log.info("_type : " + type);

        SidoDTO rDTO = animalAPIService.getSidoList(numOfRows,pageNo,type,serviceKey);

        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".getSidoList end!");

        return rDTO;
    }

    @Override
    public SigunguDTO getSigunguList(SigunguDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getSigunguList start!");

        String uprCd = CmmUtil.nvl(pDTO.uprCd());
        String serviceKey = CmmUtil.nvl(pDTO.serviceKey());
        String type = CmmUtil.nvl(pDTO.type());

        log.info("uprCd : " + uprCd);
        log.info("serviceKey : " + serviceKey);
        log.info("_type : " + type);

        SigunguDTO rDTO = animalAPIService.getSigunguList(uprCd, type, serviceKey);

        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".getSigunguList end!");

        return rDTO;
    }

    @Override
    public ShelterDTO getShelterList(ShelterDTO pDTO) throws Exception {
        return null;
    }

    @Override
    public KindDTO getKindList(KindDTO pDTO) throws Exception {
        return null;
    }

    @Override
    public AnimalDTO getAnimalList(AnimalDTO pDTO) throws Exception {
        return null;
    }
}
