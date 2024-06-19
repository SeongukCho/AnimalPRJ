package kopo.poly.service;

import kopo.poly.dto.ShelterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IShelterService {
    void saveShelterList() throws Exception;

    Page<ShelterDTO> getShelterListAll(ShelterDTO pDTO, Pageable pageable) throws Exception;

    ShelterDTO getShelterInfo(ShelterDTO pDTO) throws Exception;
}
