package kopo.poly.service;

import kopo.poly.dto.*;

public interface IAnimalFeignService {

    SidoDTO getSidoList(SidoDTO pDTO) throws Exception;

    SigunguDTO getSigunguList(SigunguDTO pDTO) throws Exception;

    ShelterDTO getShelterList(ShelterDTO pDTO) throws Exception;

    KindDTO getKindList(KindDTO pDTO) throws Exception;

    AnimalDTO getAnimalList(AnimalDTO pDTO) throws Exception;
}
