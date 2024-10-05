package org.example.backend.domain.member.repository;

import org.example.backend.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembersRepository extends JpaRepository<Members,Long> {
    @Query("SELECT m FROM Members m WHERE m.email = :email AND m.password = :password")
    //query에 from문 뒤에는 "클레스 엔티티"이름이 와랴함!!
    Optional<Members> findByLoginInfo(@Param("email") String email, @Param("password") String passWord);
    @Query("SELECT m FROM Members m WHERE m.email=:email")
    Optional<Members> findByEmail(@Param("email") String email);

    }