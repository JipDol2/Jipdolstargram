package jipdol2.eunstargram.image.entity;

import jipdol2.eunstargram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageJpaRepository extends JpaRepository<Image,Long> {

//    @Query("SELECT i FROM Image i WHERE i.member = :member")
//    List<Image> findByMember(@Param("findByMember") Member member);
}