package kopo.poly.repository;

import kopo.poly.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {

    // 회원 존재 여부 체크
    Optional<UserInfoEntity> findByUserId(String userId);

    Optional<UserInfoEntity> findByEmail(String email);

    // 로그인 (아이디, 비밀번호 확인)
    Optional<UserInfoEntity> findByUserIdAndPassword(String userId, String password);
    // 아이디, 이름, 이메일로 찾기
    Optional<UserInfoEntity> findByUserIdAndUserNameAndEmail(String userId, String userName, String email);

    /**
     * 유저 정보 업데이트
     *
     * @param userId 유저정보 PK
     */

    /**
     *
     * @Query 네이티브 쿼리 사용시 DB값은 변경되나 Entity값(캐시값)은 변경 x
     * @Modifying(clearAutomatically = true) 사용해서 객체 정보 초기화(Entity 정보 삭제) 후 캐시 갱신
     *
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_INFO A SET A.email = ?2, A.USER_NAME = ?3 WHERE A.USER_ID = ?1",
            nativeQuery = true)
    int updateUserInfo(String userId, String email, String userName);

}