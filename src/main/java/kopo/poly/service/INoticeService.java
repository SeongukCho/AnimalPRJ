package kopo.poly.service;

import kopo.poly.dto.NoticeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface INoticeService {

    /**
     * 게시판 전체 가져오기
     */
//    List<NoticeDTO> getNoticeList();

    Page<NoticeDTO> getNoticeList(int page, int size) throws Exception;

    /**
     * 게시판 상세 정보가져오기
     *
     * @param pDTO 게시판 상세 가져오기 위한 정보
     * @param type 조회수 증가여부(true : 증가, false : 증가안함
     */
    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception;

    /**
     * 해당 게시판 수정하기
     *
     * @param pDTO 게시판 수정하기 위한 정보
     */
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    /**
     * 해당 게시판 삭제하기
     *
     * @param pDTO 게시판 삭제하기 위한 정보
     */
    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;

    /**
     * 해당 게시판 저장하기
     *
     * @param pDTO 게시판 저장하기 위한 정보
     */
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;

}
