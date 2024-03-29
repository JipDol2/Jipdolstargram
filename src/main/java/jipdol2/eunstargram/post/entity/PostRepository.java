package jipdol2.eunstargram.post.entity;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Post post){
        em.persist(post);
        return post.getId();
    }

    public Optional<Post> findByOne(Long id){
        return Optional.ofNullable(em.find(Post.class,id));
    }

    /**
     * Fetch Join 최적화
     */
    public List<Post> findByAll(Long id){
        return em.createQuery("SELECT p FROM Post p " +
                        "INNER JOIN p.member m INNER JOIN FETCH p.image " +
                        "WHERE m.id = :id"+
                        " AND m.deleteYn = 'N'")
                .setParameter("id",id)
                .getResultList();
    }

}
