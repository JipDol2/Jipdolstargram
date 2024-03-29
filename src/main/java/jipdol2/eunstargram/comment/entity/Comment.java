package jipdol2.eunstargram.comment.entity;

import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long likeNumber;

    private String deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMEBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Builder
    public Comment(String content, Long likeNumber,String deleteYn, Post post, Member member) {
        this.content = content;
        this.likeNumber = likeNumber;
        this.deleteYn = deleteYn;
        checkPost(post);
        checkMember(member);
    }

    private void checkPost(Post post) {
        if(this.post != null){
            this.post.getComments().remove(this);
        }
        this.post = post;
        post.getComments().add(this);
    }

    private void checkMember(Member member) {
        if(this.member != null){
            this.member.getComments().remove(this);
        }
        this.member = member;
        member.getComments().add(this);
    }


}
