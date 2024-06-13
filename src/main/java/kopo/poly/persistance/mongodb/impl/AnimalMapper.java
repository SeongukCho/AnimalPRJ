package kopo.poly.persistance.mongodb.impl;

import com.mongodb.client.MongoCollection;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.persistance.mongodb.AbstractMongoDBCommon;
import kopo.poly.persistance.mongodb.IAnimalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnimalMapper extends AbstractMongoDBCommon implements IAnimalMapper {

    private final MongoTemplate mongodb;
    @Override
    public List<AnimalDTO> getAnimalList(String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalList Mapper Start!");

        super.createCollection(mongodb, colNm, "collectTime");

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        log.info(this.getClass().getName() + ".getAnimalList Mapper End!");

        return null;
    }
}
