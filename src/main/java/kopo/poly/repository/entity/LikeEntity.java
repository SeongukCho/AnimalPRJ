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
@Table(name = "LIKES")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
@IdClass(LikePK.class)
public class LikeEntity {

    @Id
    @Column(name = "board_seq")
    private Long boardSeq;

    @Id
    @Column(name = "user_seq")
    private Long userSeq;
}