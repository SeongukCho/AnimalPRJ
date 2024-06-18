package kopo.poly.repository;

import kopo.poly.repository.entity.BoardImgEntity;
import kopo.poly.repository.entity.ImagePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardImgRepository extends JpaRepository<BoardImgEntity, ImagePK> {

    /**
     * 특정 boardSeq에 대한 모든 이미지 찾기
     *
     * @param boardSeq 공지사항 PK
     */
    List<BoardImgEntity> findByBoardSeq(Long boardSeq);

    /**
     * 특정 imageSeq에 대한 이미지 찾기
     *
     * @param imageSeq 이미지 번호
     */
    BoardImgEntity findByImageSeq(Long imageSeq);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COALESCE(MAX(IMAGE_SEQ), 0)+1 FROM BOARD_IMAGE WHERE BOARD_SEQ = ?1",
            nativeQuery = true)
    Long getNextImageSeq(Long boardSeq);
}
