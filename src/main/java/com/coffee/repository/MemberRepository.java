package com.coffee.repository;

import com.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// 회원 정보들을 이용하여 데이터베이스와 교신하는 인터페이스입니다.
// 이전의 Dao 역할
// JpaRepository<엔터티이름, 해당엔터티의기본키변수의타입>
public interface MemberRepository extends JpaRepository<Member, Long> {
    // findByEmail를 JPA에서는 '쿼리 메소드'라고 부릅니다.
    // 이메일 정보를 이용하여 해당 회원이 존재하는 지 체크합니다.
    Member findByEmail(String email);
}
