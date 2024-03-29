package jipdol2.eunstargram.member.entity;

import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.crypto.MyPasswordEncoder;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberEmail;

    private String password;

    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String deleteYn;

    private String originalFileName;

    private String storedFileName;

    private int socialId;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<Image> image = new ArrayList<>();

    /**
     * cascade option
     * (https://www.baeldung.com/jpa-cascade-types)
     * - 문제 :  AuthControllerTest 에서 loginTest3 을 실행할때 AuthResolver 에서
     * session 을 찾지 못하는 문제가 발생했었음
     * - 해결 :  cascade option 을 정의해 주지 않았더라면 session 객체도 persist
     * 해줘야 하지만 addSession 에서 member 에만 session 을 추가해준 상태로
     * session 을 persist 해주지 않았음.
     * 따라서 cascade option 을 추가해주어서 해결할 수 있음
     */
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Session> sessions = new ArrayList<>();

    @Builder
    public Member(String memberEmail, String password, String nickname, String phoneNumber, String birthDay, String intro) {
        this.memberEmail = memberEmail;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.deleteYn = "N";
    }

    public void changeDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public void encryptPassword(MyPasswordEncoder encoder) {
        this.password = encoder.encrypt(this.password);
    }

    public static Member transferMember(MemberSaveRequestDTO memberSaveRequestDTO) {
        return new Member(
                memberSaveRequestDTO.getMemberEmail(),
                memberSaveRequestDTO.getPassword(),
                memberSaveRequestDTO.getNickname(),
                memberSaveRequestDTO.getPhoneNumber(),
                memberSaveRequestDTO.getBirthDay(),
                memberSaveRequestDTO.getIntro()
        );
    }

    public void updateMember(MemberUpdateRequestDTO updateRequestDTO) {
        this.password = updateRequestDTO.getPassword();
        this.phoneNumber = updateRequestDTO.getPhoneNumber();
        this.birthDay = updateRequestDTO.getBirthDay();
        this.intro = updateRequestDTO.getIntro();
        this.deleteYn = updateRequestDTO.getDeleteYn();
    }

    public void updateProfileImage(String originalFileName,String storedFileName){
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }

}
