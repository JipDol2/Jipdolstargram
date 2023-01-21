package jipdol2.eunstargram.post.entity;

import jipdol2.eunstargram.post.dto.PostDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Post post){
        em.persist(post);
        return post.getSeq();
    }

    public Optional<Post> findByOne(Long seq){
        return Optional.ofNullable(em.find(Post.class,seq));
    }

    public List<PostDTO> findByAll(Long seq){
        return em.createQuery("SELECT new jipdol2.eunstargram.post.dto.PostDTO(a.seq,a.imagePath,a.likeNumber,a.content,m.seq) " +
                        "FROM Post a INNER JOIN a.member m WHERE m.seq = :seq")
                .setParameter("seq",seq)
                .getResultList();
    }

    public List<PostDTO> findByAll(String memberId){
        return em.createQuery("SELECT new jipdol2.eunstargram.post.dto.PostDTO(p.seq,p.imagePath,p.likeNumber,p.content,m.seq) " +
                        "FROM Post p INNER JOIN p.member m WHERE m.memberId = :memberId")
                .setParameter("memberId",memberId)
                .getResultList();
    }

}
