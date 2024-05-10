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
    @Column(name = "user_name", length = 500, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @NonNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "reg_id", updatable = false)
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "chg_id")
    private String chgId;

    @Column(name = "chg_dt")
    private String chgDt;
}
