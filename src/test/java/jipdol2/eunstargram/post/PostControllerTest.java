package jipdol2.eunstargram.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.entity.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String COMMON_URL = "/api/post";

    @BeforeEach
    void clean(){
        this.entityManager
                .createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("게시글 업로드 : 게시글 업로드시 200 status code 리턴")
    @Transactional
    void uploadPostTest() throws Exception {

        //given
        Member member = createMember();
        memberRepository.save(member);

        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(COMMON_URL+ "/upload")
                        .file((MockMultipartFile)postSaveRequestDTO.getImage())
                        .param("likeNumber",Long.toString(postSaveRequestDTO.getLikeNumber()))
                        .param("content",postSaveRequestDTO.getContent())
                        .param("memberId",Long.toString(postSaveRequestDTO.getMemberId()))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(postRepository.findByAll(1l).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 전체 조회 : 게시글 전체 조회 200 status code 리턴 + 게시글 리턴")
    @Transactional
    void findByAllPosts() throws Exception{

        //given
        Member member = createMember();
        memberRepository.save(member);

        List<PostSaveRequestDTO> postRequestListDTO = createPostRequestListDTO();
        postRequestListDTO.stream().forEach(postService::save);

//        String json = objectMapper.writeValueAsString(postSaveRequestDTO);


        //when
        mockMvc.perform(MockMvcRequestBuilders.get(COMMON_URL+"/{id}","1"))
                .andExpect(jsonPath("$[0].likeNumber").value(0L))
                .andExpect(jsonPath("$[0].content").value("행복한 하루"))
                .andExpect(jsonPath("$[0].memberId").value(1L))
                .andExpect(jsonPath("$[0].imageDTO.originalFileName").value("testImage.jpg"))
                .andExpect(jsonPath("$[1].likeNumber").value(0L))
                .andExpect(jsonPath("$[1].content").value("웃는 하루"))
                .andExpect(jsonPath("$[1].memberId").value(1L))
                .andExpect(jsonPath("$[1].imageDTO.originalFileName").value("testImage.jpg"))
                .andDo(print());
        //then
        assertThat(postRepository.findByAll(1l).size()).isEqualTo(2);
    }

    private Member createMember() {
        Member member = Member.builder()
                .memberId("testId")
                .password("1234")
                .nickname("Rabbit96")
                .birthDay("19940715")
                .intro("life is one time")
                .deleteYn("N")
                .build();
        return member;
    }

    private PostSaveRequestDTO createPostRequestDTO() throws Exception{
        PostSaveRequestDTO postSaveRequestDTO = PostSaveRequestDTO.builder()
                .content("행복한 하루")
                .memberId(1L)
                .image(createMultipartFile())
                .build();
        return postSaveRequestDTO;
    }

    private List<PostSaveRequestDTO> createPostRequestListDTO() throws Exception{
        return List.of(
                PostSaveRequestDTO.builder()
                        .content("행복한 하루")
                        .memberId(1L)
                        .image(createMultipartFile())
                        .build(),
                PostSaveRequestDTO.builder()
                        .content("웃는 하루")
                        .memberId(1L)
                        .image(createMultipartFile())
                        .build()
        );
    }

    private MockMultipartFile createMultipartFile() throws Exception {
        String originalFileName = "testImage.jpg";
        String path = "src/test/resources/img/" + originalFileName;

        return new MockMultipartFile("image", originalFileName, "image/jpg",new FileInputStream(path));
    }

}