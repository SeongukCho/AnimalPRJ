package kopo.poly.service;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;

import java.util.List;
import java.util.Map;

public interface IAnimalService {
    List<AnimalDTO> fetchAnimalData(AnimalDTO pDTO) throws Exception;
    List<ShelterDTO> fetchShelterData(ShelterDTO pDTO) throws Exception;
}

