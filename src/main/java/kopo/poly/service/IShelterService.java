package kopo.poly.service;

import kopo.poly.dto.ShelterInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IShelterService {
    void saveShelterList() throws Exception;

    Page<ShelterInfoDTO> getShelterListAll(ShelterInfoDTO pDTO, Pageable pageable) throws Exception;

    ShelterInfoDTO getShelterInfo(ShelterInfoDTO pDTO) throws Exception;
}
