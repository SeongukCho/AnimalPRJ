package kopo.poly.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@Getter
public class LikePK implements Serializable {

    private long boardSeq;
    private long userSeq;

    public LikePK(long boardSeq, long userSeq) {
        this.boardSeq = boardSeq;
        this.userSeq = userSeq;
    }
}