package core.backendstudyToyService.domain.board.controller;


import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor//현재 구현체가 하나이므로 생성자 주입을 사용했습니다
//별도의 서비스 구현체가 있다면 바꿔주세요
public class PostController {
//메롱
    private PostService postService;

    /**
     * 게시글에 포함된 댓글목록,좋아요 수를 같이 반환합니다
     * @param postId 게시글 번호가 들어옵니다
     * @param model 대빵많이 해야되니까 모델이 필요해요
     * @return 게시글 번호를 등록하면 디테일폼으로 반환합니다
     */
    @GetMapping("/posts/{postId}")
    public String getPostDetails(@PathVariable Long postId, Model model){
        try {
            PostDetailsDTO postDetailsDTO = postService.getPostDetails(postId);
            //이미지 프로세싱은 보류
            model.addAttribute("postDetails",postDetailsDTO);
            return "post-details";
        }catch (RuntimeException e){
            model.addAttribute("에러!",e.getMessage());
            return "error";
        }
    }

    @GetMapping("/posts")
    public String getPostList(Model model){
        model.addAttribute("posts",postService.findAllPosts());
        return "post-list";
    }

    /**
     * 게시글에 좋아요를 등록합니다
     * 유저 세션정보에 따라 좋아요 결과가 반영됩니다
     * @param postId 게시글 번호
     * @return 리다이렉션 결과를 반환합니다
     */
    @PostMapping("/posts/{postId}/likes")
    public String addLikePost(@PathVariable Long postId){
        Long userId = 1L;
        postService.addLikePost(postId,userId);
        return "redirect:/posts/"+postId;
    }//세션정보가 추가되면 파라미터에 추가해주세요!

    /**
     * 게시글 댓글을 등록합니다
     * 유저 세션정보가 있으면 작성자도 포함됩니다
     * @param postId 게시글번호
     * @param content 댓글내용
     * @return 리다이렉션 결과를 반환합니다
     */
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId,@RequestParam String content){
        Long userId = 1L;
        postService.addCommentPost(postId,userId,content);
        return "redirect:/posts/"+postId;
    }//세션정보가 추가되면 파라미터에 추가해주세요!
}
