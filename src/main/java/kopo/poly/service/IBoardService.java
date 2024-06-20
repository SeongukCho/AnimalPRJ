package kopo.poly.service;

import kopo.poly.dto.BoardDTO;
import kopo.poly.dto.BoardImgDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBoardService {

    /**
     * 게시판 전체 가져오기
     */
//    List<BoardDTO> getBoardList();

    Page<BoardDTO> getBoardList(int page, int size) throws Exception;

    /**
     * 게시판 상세 정보가져오기
     *
     * @param pDTO 게시판 상세 가져오기 위한 정보
     * @param type 조회수 증가여부(true : 증가, false : 증가안함
     */
    BoardDTO getBoardInfo(BoardDTO pDTO, boolean type) throws Exception;

    /**
     * 해당 게시판 수정하기
     *
     * @param pDTO 게시판 수정하기 위한 정보
     */
    void updateBoardInfo(BoardDTO pDTO) throws Exception;

    /**
     * 해당 게시판 삭제하기
     *
     * @param pDTO 게시판 삭제하기 위한 정보
     */
    void deleteBoardInfo(BoardDTO pDTO) throws Exception;

    /**
     * 해당 게시판 저장하기
     *
     * @param pDTO 게시판 저장하기 위한 정보
     */
    Long insertBoardInfo(BoardDTO pDTO) throws Exception;

    /**
     * 이미지 리스트 저장
     *
     * @param boardSeq 공지사항 번호
     * @param imageDTOs 이미지 리스트
     */
    void updateBoardImages(Long boardSeq, List<BoardImgDTO> imageDTOs) throws Exception;

    /**
     * 해당 게시글의 이미지 리스트 가져오기
     */
    List<BoardImgDTO> getImageList(BoardDTO pDTO) throws Exception;

    /**
     * 게시글 이미지 삭제
     */
    void deleteImageById(BoardImgDTO pDTO) throws Exception;

    /**
     * 게시글 이미지 S3에서 삭제하기 위해 이미지 url를 추출
     */
    String getImagePath(BoardImgDTO pDTO) throws Exception;

    /**
     * 게시글을 삭제하면 이미지들을 S3에서 삭제하기 위해 이미지 리스트 추출
     */
    List<BoardImgDTO> getImagePathList(Long boardSeq) throws Exception;

    /**
     * 특정 userId 기준 게시글 개수 조회
     */
    long countByUserId(String userId) throws Exception;

    /**
     * NativeQuery 사용하여 내 게시글만 가져오기
     */
    List<BoardDTO> getUserNoticeListUsingNativeQuery(String userId);
}
