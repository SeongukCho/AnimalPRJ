package kopo.poly.controller;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.impl.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/animal")
@RequiredArgsConstructor
@RestController
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/animal")
    public List<AnimalDTO> getAnimalData(
            @RequestParam AnimalDTO rDTO) throws Exception {
        return animalService.fetchAnimalData(rDTO);
    }

    @GetMapping("/shelter")
    public List<ShelterDTO> getShelterData(
            @RequestParam String uprCd,
            @RequestParam String orgCd) throws Exception {
        return animalService.fetchShelterData(rDTO);
    }
}