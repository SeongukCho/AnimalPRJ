package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOARD_IMAGE")
@DynamicInsert // 값이 NULL이 아닌것만 INSERT함
@DynamicUpdate // 값이 NULL이 아닌것만 UPDATE함
@Builder
//@Cacheable
@IdClass(ImagePK.class)
@Entity
public class BoardImgEntity {

    @Id
    @Column(name = "board_seq")
    private Long boardSeq;

    @Id
    @Column(name = "image_seq")
    private Long imageSeq;

    @Column(name = "image_path", nullable = false)
    private String imagePath;
}
