package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.BoardDTO;
import kopo.poly.dto.BoardImgDTO;
import kopo.poly.repository.BoardImgRepository;
import kopo.poly.repository.BoardRepository;
import kopo.poly.repository.entity.BoardEntity;
import kopo.poly.repository.entity.BoardImgEntity;
import kopo.poly.repository.entity.ImagePK;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService implements IBoardService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // BoardRepository 변수에 이미 메모리에 올라간 BoardRepository 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;

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

        Pageable pageable = PageRequest.of(page - 1, size);
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

    @Transactional
    @Override
    public Long insertBoardInfo(BoardDTO pDTO) throws Exception {

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

        return pEntity.getBoardSeq();
    }

    @Override
    @Transactional
    public void updateBoardImages(Long boardSeq, List<BoardImgDTO> imageDTOs) throws Exception {

        log.info(this.getClass().getName() + ".updateBoardImages Start!");

        // 기존 이미지 목록을 가져옵니다.
        List<BoardImgEntity> existingImages = boardImgRepository.findByBoardSeq(boardSeq);

        // 데이터베이스에 이미 있는 이미지들의 처리
        Set<Long> existingImageIds = existingImages.stream()
                .map(BoardImgEntity::getImageSeq)
                .collect(Collectors.toSet());

        Long nextImageSeq = boardImgRepository.getNextImageSeq(boardSeq);

        // 새 이미지 추가 또는 기존 이미지 업데이트
        AtomicLong imageSeqCounter = new AtomicLong(nextImageSeq);
        List<BoardImgEntity> imagesToSave = imageDTOs.stream()
                .map(dto -> {

                    Long newImageSeq = imageSeqCounter.getAndIncrement(); // nextImageSeq부터 시작해서 1씩 증가함
                    log.info("newImageSeq : " + newImageSeq);

                    // 기존 이미지 엔티티 업데이트 또는 새 엔티티 생성
                    BoardImgEntity imageEntity = existingImages.stream()
                            .filter(img -> img.getImageSeq() != null && img.getImageSeq().equals(dto.imageSeq()))
                            .findFirst()
                            .orElseGet(() -> BoardImgEntity.builder()
                                    .imageSeq(newImageSeq)
                                    .boardSeq(boardSeq) // 새 이미지의 경우 boardSeq 설정
                                    .build());

                    // 빌더를 통해 imagePath 설정
                    return BoardImgEntity.builder()
                            .imageSeq(imageEntity.getImageSeq()) // 기존 ID 유지, null이면 자동 생성
                            .boardSeq(boardSeq)
                            .imagePath(dto.imagePath())
                            .build();
                })
                .collect(Collectors.toList());

        boardImgRepository.saveAll(imagesToSave);

        existingImages.stream()
                .filter(image -> !existingImageIds.contains(image.getImageSeq()))
                .forEach(boardImgRepository::delete);

        log.info(this.getClass().getName() + ".updateNoticeImages End!");

    }

    @Transactional
    @Override
    public List<BoardImgDTO> getImageList(BoardDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateBoardImages Start!");

        Long boardSeq = pDTO.boardSeq();
        log.info("boardSeq : " + boardSeq);

        List<BoardImgEntity> rList = boardImgRepository.findByBoardSeq(boardSeq);

        List<BoardImgDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<BoardImgDTO>>() {
                });

        log.info(this.getClass().getName() + ".updateBoardImages End!");

        return nList;
    }

    @Override
    @Transactional
    public void deleteImageById(BoardImgDTO pDTO) throws Exception {

        ImagePK imagePK = ImagePK.builder().imageSeq(pDTO.imageSeq()).boardSeq(pDTO.boardSeq()).build();

        BoardImgEntity imageEntity = boardImgRepository.findById(imagePK)
                .orElseThrow(() -> new Exception("Image not found with ID: " + imagePK));

        // 데이터베이스에서 이미지 레코드 삭제
        boardImgRepository.delete(imageEntity);
    }

    /**
     * 게시글 이미지 S3에서 삭제하기 위해 이미지 url를 추출
     */
    @Override
    public String getImagePath(BoardImgDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "getImagePath Start!");

        ImagePK imagePK = ImagePK.builder().boardSeq(pDTO.boardSeq()).imageSeq(pDTO.imageSeq()).build();

        Optional<BoardImgEntity> rEntity = boardImgRepository.findById(imagePK);

        String imagePath = rEntity.get().getImagePath();

        log.info("imagePath : " + imagePath);

        log.info(this.getClass().getName() + "getImagePath End!");

        return imagePath;
    }

    /**
     * 게시글을 삭제하면 이미지들을 S3에서 삭제하기 위해 이미지 리스트 추출
     */
    @Override
    public List<BoardImgDTO> getImagePathList(Long boardSeq) throws Exception {

        log.info(this.getClass().getName() + "getImagePathList Start!");

        List<BoardImgEntity> rList = boardImgRepository.findByBoardSeq(boardSeq);

        List<BoardImgDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<BoardImgDTO>>() {
                });

        log.info(this.getClass().getName() + "getImagePathList End!");

        return nList;
    }

    /**
     * 특정 userId 기준 게시글 개수 조회
     */
    @Override
    public long countByUserId(String userId) throws Exception {
        return boardRepository.countByUserId(userId);
    }
}
