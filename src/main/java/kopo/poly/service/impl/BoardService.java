package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.BoardDTO;
import kopo.poly.repository.BoardRepository;
import kopo.poly.repository.entity.BoardEntity;
import kopo.poly.service.IBoardService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService implements IBoardService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // BoardRepository 변수에 이미 메모리에 올라간 BoardRepository 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final BoardRepository boardRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Override
//    public List<BoardDTO> getBoardList() {
//
//        log.info(this.getClass().getName() + ".getBoardList Start!");
//
//        // 게시판 전체 리스트 조회하기
//        List<BoardEntity> rList = boardRepository.findAllByOrderByBoardSeqDesc();
//
//        // 엔티티의 값들을 DTO에 맞게 넣어주기
//        List<BoardDTO> nList = new ObjectMapper().convertValue(rList,
//                new TypeReference<>() {
//                });
//
//        log.info(this.getClass().getName() + ".getBoardList End!");
//
//        return nList;
//    }

    public Page<BoardDTO> getBoardList(int page, int size) {
        log.info(this.getClass().getName() + ".getBoardList with Pagination Start!");

        Pageable pageable = PageRequest.of(page, size);
        Page<BoardEntity> entityPage = boardRepository.findAllByOrderByBoardSeqDesc(pageable);
        Page<BoardDTO> dtoPage = entityPage.map(entity -> objectMapper.convertValue(entity, BoardDTO.class));

        log.info(this.getClass().getName() + ".getBoardList with Pagination End!");

        return dtoPage;
    }

    @Transactional
    @Override
    public BoardDTO getBoardInfo(BoardDTO pDTO, boolean type) {

        log.info(this.getClass().getName() + ".getBoardInfo Start!");

        if (type) {
            // 조회수 증가하기
            int res = boardRepository.updateReadCnt(pDTO.boardSeq());

            // 조회수 증가 성공여부 체크
            log.info("res : " + res);
        }

        // 게시판 상세내역 가져오기
        BoardEntity rEntity = boardRepository.findByBoardSeq(pDTO.boardSeq());

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        BoardDTO rDTO = new ObjectMapper().convertValue(rEntity, BoardDTO.class);

        log.info(this.getClass().getName() + ".getBoardInfo End!");

        return rDTO;
    }

    @Transactional
    @Override
    public void updateBoardInfo(BoardDTO pDTO) {

        log.info(this.getClass().getName() + ".updateBoardInfo Start!");

        Long boardSeq = pDTO.boardSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("boardSeq : " + boardSeq);
        log.info("title : " + title);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        // 현재 게시판 조회수 가져오기
        BoardEntity rEntity = boardRepository.findByBoardSeq(boardSeq);

        // 수정할 값들을 빌더를 통해 엔티티에 저장하기
        BoardEntity pEntity = BoardEntity.builder()
                .boardSeq(boardSeq)
                .title(title)
                .contents(contents)
                .userId(userId)
                .readCnt(rEntity.getReadCnt())
                .chgId(userId)
                .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 데이터 수정하기
        boardRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateBoardInfo End!");

    }

    @Override
    public void deleteBoardInfo(BoardDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteBoardInfo Start!");

        Long boardSeq = pDTO.boardSeq();

        log.info("boardSeq : " + boardSeq);

        // 데이터 수정하기
        boardRepository.deleteById(boardSeq);


        log.info(this.getClass().getName() + ".deleteBoardInfo End!");
    }

    @Override
    public void insertBoardInfo(BoardDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertBoardInfo Start!");

        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("title : " + title);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        // 게시판 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        BoardEntity pEntity = BoardEntity.builder()
                .title(title)
                .contents(contents)
                .userId(userId)
                .readCnt(0L)
                .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 게시판 저장하기
        boardRepository.save(pEntity);

        log.info(this.getClass().getName() + ".InsertBoardInfo End!");

    }
}
