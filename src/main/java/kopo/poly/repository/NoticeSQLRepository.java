package kopo.poly.repository;

import kopo.poly.repository.entity.BoardSQLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeSQLRepository extends JpaRepository<BoardSQLEntity, Long> {



    @Query(value = "SELECT N.BOARD_SEQ, N.TITLE, N.READ_CNT, N.USER_ID, N.REG_ID, N.REG_DT, N.CHG_ID, N.CHG_DT, " +
            "U.USER_NAME, U.PROFILE_PATH, I.IMAGE_PATH " +
            "FROM AnimalDB.BOARD N JOIN AnimalDB.USER_INFO U ON N.USER_ID = U.USER_ID LEFT JOIN ( " +
            "SELECT BOARD_SEQ, IMAGE_PATH, ROW_NUMBER() OVER (PARTITION BY BOARD_SEQ ORDER BY IMAGE_SEQ) AS rn FROM AnimalDB.BOARD_IMAGE) I " +
            "ON N.BOARD_SEQ = I.BOARD_SEQ AND I.rn = 1 " +
            "WHERE N.USER_ID = :userId " +
            "ORDER BY N.BOARD_SEQ DESC", nativeQuery = true)
    List<BoardSQLEntity> getUserNoticeListUsingSQL(@Param("userId") String userId);



}
