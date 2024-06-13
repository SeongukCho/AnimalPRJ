package kopo.poly.persistance.mongodb;

import kopo.poly.dto.AnimalDTO;

import java.util.List;

public interface IAnimalMapper {

    List<AnimalDTO> getAnimalList(String colNm) throws Exception;


}
