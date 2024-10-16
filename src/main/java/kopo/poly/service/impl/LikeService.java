package kopo.poly.service.impl;

import kopo.poly.dto.LikeDTO;
import kopo.poly.repository.LikeRepository;
import kopo.poly.repository.entity.LikeEntity;
import kopo.poly.repository.entity.LikePK;
import kopo.poly.service.ILikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class LikeService implements ILikeService {

    private final LikeRepository likeRepository;
    @Override
    public LikeDTO likeExists(LikeDTO pDTO) {

        log.info(this.getClass().getName() + ".likeExists Start!");

        LikeDTO rDTO;

        long userSeq = pDTO.userSeq();
        long boardSeq = pDTO.boardSeq();

        log.info("userSeq : " + userSeq);
        log.info("boardSeq : " + boardSeq);

        LikePK likePK = LikePK.builder()
                .boardSeq(boardSeq)
                .userSeq(userSeq)
                .build();

        Optional<LikeEntity> rEntity = likeRepository.findById(likePK);

        if (rEntity.isPresent()) {
            rDTO = LikeDTO.builder().existsYn("Y").build();
        } else {
            rDTO = LikeDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".likeExists End!");

        return rDTO;
    }

    @Override
    public void insertLike(LikeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertLike Start!");

        long userSeq = pDTO.userSeq();
        Long boardSeq = pDTO.boardSeq();

        log.info("userSeq : " + userSeq);
        log.info("boardSeq : " + boardSeq);

        LikeEntity pEntity = LikeEntity.builder()
                .boardSeq(boardSeq)
                .userSeq(userSeq)
                .build();

        likeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".insertLike End!");

    }

    @Override
    public void deleteLike(LikeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteLike Start!");

        Long userSeq = pDTO.userSeq();
        Long boardSeq = pDTO.boardSeq();

        log.info("userSeq : " + userSeq);
        log.info("boardSeq : " + boardSeq);

        LikePK likePK = LikePK.builder()
                .boardSeq(boardSeq)
                .userSeq(userSeq)
                .build();

        // 좋아요 삭제하기
        likeRepository.deleteById(likePK);

        log.info(this.getClass().getName() + ".deleteLike End!");

    }


}