package com.encore.board.repository;

import com.encore.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedTimeDesc();

    // SQL: SELECT p.* FROM POST p LEFT JOIN MEMBER m ON p.member_id = m.id;
    // 아래 JPQL의 join문은 Member 객체를 통해 Post를 스크리닝하고 싶은 상황에 적합
    @Query("select p from Post p left join p.member order by p.createdTime desc") // JPQL문
    List<Post> findAllGeneralJoin();
    @Query("select p from Post p left join fetch p.member order by p.createdTime desc")
    List<Post> findAllFetchJoin();

}
