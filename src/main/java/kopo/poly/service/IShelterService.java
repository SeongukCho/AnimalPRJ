package kopo.poly.service;

import kopo.poly.dto.ShelterDTO;

import java.util.List;

public interface IShelterService {
    List<ShelterDTO> getShelterList(ShelterDTO pDTO) throws Exception;
}
