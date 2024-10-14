package kopo.poly.service;

import kopo.poly.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAnimalService {

    void saveAnimalList();

    void saveNoticeAnimal();

    void saveProtectAnimal();
    Page<AnimalDTO> getAnimalListAll(AnimalDTO pDTO, Pageable pageable, String collectionName) throws Exception;
    AnimalDTO getAnimalInfo(AnimalDTO pDTO) throws Exception;
//    List<AnimalDTO> searchAnimalList(AnimalDTO pDTO) throws Exception;
}

