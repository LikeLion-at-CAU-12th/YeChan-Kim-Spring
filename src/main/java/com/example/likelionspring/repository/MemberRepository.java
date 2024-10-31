package com.example.likelionspring.repository;

import com.example.likelionspring.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);

    Page<Member> findByAgeGreaterThanEqual(int age, Pageable pageable);

    Page<Member> findByUsernameStartingWith(String prefix, Pageable pageable);

    boolean existsByUsername(String username);
}
