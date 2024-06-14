package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.CommentDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.BoardDTO;
import kopo.poly.service.ICommentService;
import kopo.poly.service.IBoardService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Controller 선언해야만 Spring 프레임워크에서 Controller인지 인식 가능
 * 자바 서블릿 역할 수행
 *
 * slf4j는 스프링 프레임워크에서 로그 처리하는 인터페이스 기술이며,
 * 로그처리 기술인 log4j와 logback과 인터페이스 역할 수행함
 * 스프링 프레임워크는 기본으로 logback을 채택해서 로그 처리함
 * */
@Slf4j
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함
    private final IBoardService boardService;
    private final ICommentService commentService;

    /**
     * 게시판 리스트 보여주기
     * <p>
     * GetMapping(value = "Board/BoardList") =>  GET방식을 통해 접속되는 URL이 Board/BoardList 경우 아래 함수를 실행함
     */
    @GetMapping(value = "boardList")
    public String BoardList(HttpSession session, ModelMap model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".boardList Start!");

        // 로그인된 사용자 아이디는 Session에 저장함
        // 교육용으로 아직 로그인을 구현하지 않았기 때문에 Session에 데이터를 저장하지 않았음
        // 추후 로그인을 구현할 것으로 가정하고, 게시판 리스트 출력하는 함수에서 로그인 한 것처럼 Session 값을 생성함
//        session.setAttribute("SESSION_USER_ID", "USER01");

        // 페이징된 게시판 리스트 조회하기
        Page<BoardDTO> rList = boardService.getBoardList(page, size);

        log.info("rList : " + rList);
        log.info("rList.getTotalPages() : " + rList.getTotalPages());

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", rList.getTotalPages());

        // 페이지 번호 범위 계산
        int startPage = Math.max(1, page - 4);
        int endPage = Math.min(rList.getTotalPages(), page + 5);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".boardList End!");

        // 함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        // templates/board/boardList.html
        return "board/boardList";
    }

    /**
     * 게시판 작성 페이지 이동
     * <p>
     * 이 함수는 게시판 작성 페이지로 접근하기 위해 만듬
     * <p>
     * GetMapping(value = "board/boardReg") =>  GET방식을 통해 접속되는 URL이 board/boardReg 경우 아래 함수를 실행함
     */
    @GetMapping(value = "boardReg")
    public String BoardReg() {

        log.info(this.getClass().getName() + ".boardReg Start!");

        log.info(this.getClass().getName() + ".boardReg End!");

        // 함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        // templates/board/boardReg.html
        return "board/boardReg";
    }

    /**
     * 게시판 글 등록
     * <p>
     * 게시글 등록은 Ajax로 호출되기 때문에 결과는 JSON 구조로 전달해야만 함
     * JSON 구조로 결과 메시지를 전송하기 위해 @ResponseBody 어노테이션 추가함
     */
    @ResponseBody
    @PostMapping(value = "boardInsert")
    public MsgDTO boardInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".boardInsert Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto; // 결과 메시지 구조

        try {
            // 로그인된 사용자 아이디를 가져오기
            // 로그인을 아직 구현하지 않았기에 게시판 리스트에서 로그인 한 것처럼 Session 값을 저장함
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("session user_id : " + userId);
            log.info("title : " + title);
            log.info("contents : " + contents);

            // 데이터 저장하기 위해 DTO에 저장하기
            BoardDTO pDTO = BoardDTO.builder()
                    .userId(userId)
                    .title(title)
                    .contents(contents).build();

            /*
             * 게시글 등록하기위한 비즈니스 로직을 호출
             */
            boardService.insertBoardInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다.";

        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".boardInsert End!");
        }

        return dto;
    }

    /**
     * 게시판 상세보기
     */
    @GetMapping(value = "boardInfo")
    public String boardInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".boardInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"), "0"); // 공지글번호(PK)

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("nSeq : " + nSeq);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        BoardDTO pDTO = BoardDTO.builder()
                .boardSeq(Long.parseLong(nSeq))
                .build();

        CommentDTO cDTO = CommentDTO.builder()
                .boardSeq(Long.parseLong(nSeq))
                .build();

        // 게시판 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        BoardDTO rDTO = Optional.ofNullable(boardService.getBoardInfo(pDTO, true))
                .orElseGet(() -> BoardDTO.builder().build());

        List<CommentDTO> cList = Optional.ofNullable(commentService.getCommentList(cDTO))
                .orElseGet(() -> new ArrayList<>());

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);
        model.addAttribute("cList", cList);

        log.info(this.getClass().getName() + ".boardInfo End!");

        return "board/boardInfo";
    }

    /**
     * 게시판 수정 보기
     */
    @GetMapping(value = "boardEditInfo")
    public String boardEditInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".boardEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("nSeq : " + nSeq);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();

        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        BoardDTO rDTO = Optional.ofNullable(boardService.getBoardInfo(pDTO, false))
                .orElseGet(() -> BoardDTO.builder().build());

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".boardEditInfo End!");

        return "board/boardEditInfo";
    }

    /**
     * 게시판 글 수정
     */
    @ResponseBody
    @PostMapping(value = "boardUpdate")
    public MsgDTO boardUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".boardUpdate Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 아이디
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("userId : " + userId);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("contents : " + contents);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            BoardDTO pDTO = BoardDTO.builder()
                    .userId(userId)
                    .boardSeq(Long.parseLong(nSeq))
                    .title(title)
                    .contents(contents)
                    .build();

            // 게시글 수정하기 DB
            boardService.updateBoardInfo(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            // 결과 메시지 전달하기
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".boardUpdate End!");

        }

        return dto;
    }

    /**
     * 게시판 글 삭제
     */
    @ResponseBody
    @PostMapping(value = "boardDelete")
    public MsgDTO boardDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".boardDelete Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("nSeq : " + nSeq);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();

            // 게시글 삭제하기 DB
            boardService.deleteBoardInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            // 결과 메시지 전달하기
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".boardDelete End!");

        }

        return dto;
    }
}
