package kopo.poly.service;

import kopo.poly.dto.CommentDTO;
import kopo.poly.dto.BoardDTO;

import java.util.List;

public interface ICommentService {

    /**
     * 댓글 전체 가져오기
     */
    List<CommentDTO> getCommentList(CommentDTO pDTO);

    /**
     * 해당 댓글 수정하기
     *
     * @param pDTO 게시판 수정하기 위한 정보
     */
    int updateComment(CommentDTO pDTO) throws Exception;

    /**
     * 해당 댓글 삭제하기
     *
     * @param pDTO 게시판 삭제하기 위한 정보
     */
    void deleteComment(CommentDTO pDTO) throws Exception;

    /**
     * 해당 댓글 저장하기
     *
     * @param pDTO 게시판 저장하기 위한 정보
     */
    void insertComment(CommentDTO pDTO) throws Exception;

}