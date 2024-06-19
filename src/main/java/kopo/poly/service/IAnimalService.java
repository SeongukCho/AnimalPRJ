package kopo.poly.service;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IAnimalService {

    void saveAnimalList();

    void saveNoticeAnimal();

    void saveProtectAnimal();
    Page<AnimalDTO> getAnimalListAll(AnimalDTO pDTO, Pageable pageable, String collectionName) throws Exception;
    AnimalDTO getAnimalInfo(AnimalDTO pDTO) throws Exception;
//    List<AnimalDTO> searchAnimalList(AnimalDTO pDTO) throws Exception;
}

