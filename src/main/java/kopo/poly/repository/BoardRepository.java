package kopo.poly.repository;

import kopo.poly.repository.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /**
     * 게시판 리스트
     */
//    List<BoardEntity> findAllByOrderByBoardSeqDesc();

    /**
     * 게시판 리스트
     *
     * @param boardSeq 게시판 PK
     */
    BoardEntity findByBoardSeq(Long boardSeq);

    Page<BoardEntity> findAllByOrderByBoardSeqDesc(Pageable pageable);

    /**
     * 게시판 상세 보기할 때, 조회수 증가하기
     *
     * @param boardSeq 게시판 PK
     * @Query 네이티브 쿼리 사용시 DB값은 변경되지만 Entity의 값(캐쉬)은 변경되지 않음
     * @Modifying(celarAutomatically = true) 사용해서 객체 정보 초기화(Entity 정보 삭제) 후 캐시 갱신
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARD A SET A.READ_CNT = IFNULL(A.READ_CNT, 0) + 1 WHERE A.BOARD_SEQ = ?1",
            nativeQuery = true)
    int updateReadCnt(Long boardSeq);

    /**
     * 특정 userId 기준 게시글 개수 조회
     */
    long countByUserId(String userId);
}
