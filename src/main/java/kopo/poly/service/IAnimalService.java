package kopo.poly.service;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;

import java.util.List;
import java.util.Map;

public interface IAnimalService {

    void saveAnimalList() throws Exception;
    List<AnimalDTO> getAnimalListAll(AnimalDTO pDTO) throws Exception;

    AnimalDTO getAnimalInfo(AnimalDTO pDTO) throws Exception;
}

