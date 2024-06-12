package kopo.poly.service;

import kopo.poly.dto.BoardDTO;
import org.springframework.data.domain.Page;

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
    void insertBoardInfo(BoardDTO pDTO) throws Exception;

}
