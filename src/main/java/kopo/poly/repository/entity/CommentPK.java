package kopo.poly.repository.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@Getter
public class CommentPK implements Serializable {

    private long commentSeq;
    private long boardSeq;

    public CommentPK(long commentSeq, long boardSeq) {

        this.commentSeq = commentSeq;
        this.boardSeq = boardSeq;
    }

}