package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class UserInfoEntity implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId;

    @NonNull
    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "nickname", length = 20, nullable = false)
    private String nickName;

    @NonNull
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @NonNull
    @Column(name = "email", length = 100,nullable = false)
    private String email;

//    @Column(name = "pet_yn")
//    private String petYn;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "PROFILE_PATH")
    private String profilePath;

}
