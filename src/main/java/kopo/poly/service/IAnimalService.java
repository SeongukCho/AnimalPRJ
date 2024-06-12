package kopo.poly.service;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;

import java.util.List;
import java.util.Map;

public interface IAnimalService {
    List<AnimalDTO> getAnimalList(AnimalDTO pDTO) throws Exception;
    List<ShelterDTO> getShelterList(ShelterDTO pDTO) throws Exception;
}

