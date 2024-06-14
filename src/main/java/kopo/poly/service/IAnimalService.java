package kopo.poly.service;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IAnimalService {

    void saveAnimalList() throws Exception;
    Page<AnimalDTO> getAnimalListAll(AnimalDTO pDTO, Pageable pageable) throws Exception;

    AnimalDTO getAnimalInfo(AnimalDTO pDTO) throws Exception;
}

