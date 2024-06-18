package kopo.poly.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@NoArgsConstructor
public class ImagePK implements Serializable {

    private long imageSeq;
    private long boardSeq;

    public ImagePK(long imageSeq, long boardSeq) {
        this.imageSeq = imageSeq;
        this.boardSeq = boardSeq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImagePK imagePK = (ImagePK) o;
        return imageSeq == imagePK.imageSeq && boardSeq == imagePK.boardSeq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageSeq, boardSeq);
    }
}
