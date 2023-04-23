package jipdol2.eunstargram.common.controller;

import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.image.dto.response.ImageResponseDTO;
import jipdol2.eunstargram.member.MemberService;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.post.PostService;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberService memberService;
    private final PostService postService;

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
//        model.addAttribute("id","1");
        return "/login";
    }

    @GetMapping("/signUp")
    public String singUp(){
        return "/signUp";
    }

    @GetMapping("/posts/{nickname}")
    public String profile(@PathVariable("nickname") String nickname,Model model){
        log.info("nickname={}",nickname);
        MemberFindResponseDTO findByMember = memberService.findByMemberNickname(nickname);
        model.addAttribute("member",findByMember);

        ImageResponseDTO findByProfileImage = memberService.findByProfileImage(nickname);
        model.addAttribute("profileImage",findByProfileImage);

        List<PostResponseDTO> findByPosts = postService.findByAll(nickname);
        model.addAttribute("posts",findByPosts);
        return "/posts";
    }
}
