package core.backendstudyToyService.domain.board.controller;


import core.backendstudyToyService.domain.board.dto.PostDTO;
import core.backendstudyToyService.domain.board.service.PostService;
import core.backendstudyToyService.domain.common.Criteria;
import core.backendstudyToyService.domain.common.PageDTO;
import core.backendstudyToyService.domain.common.PagingResponseDTO;
import core.backendstudyToyService.domain.common.ResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;
    private final ModelMapper modelMapper;

    @Autowired
    public PostController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    // 1. 게시글 조회 파트
    // 1-1. 게시판 목록 조회 - 페이지별 조회(한 페이지당 10개씩 보여주기)
// 1. 게시글 조회 파트
// 1-1. 게시판 목록 조회 - 페이지별 조회(한 페이지당 10개씩 보여주기)
    @GetMapping("/post")
    public String selectBoardList(@RequestParam(name = "currentPage", required = false, defaultValue = "1") String currentPage, Model model) {
        Criteria cri = new Criteria(Integer.valueOf(currentPage), 10);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        int total = postService.selectPostTotal();

        pagingResponseDTO.setData(postService.selectPostListWithPaging(cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        model.addAttribute("pagingResponseDTO", pagingResponseDTO); // 모델에 추가

        return "post";
    }


    // 1-2. 게시글 상세 내용 조회 - 게시글 제목, 내용, 작성자, 작성일

    // 1-3. 게시글 댓글 조회

    // 1-4. 게시판 카테고리 조회



    // 2. 게시글 작성, 수정, 삭제 파트

    // 2-1. 게시글 작성 페이지 조회
    @GetMapping("/newpost")
    public String showNewPostForm(Model model) {
        model.addAttribute("post", new PostDTO());
        model.addAttribute("imageBase64", "");
        return "newpost"; // 새 게시글 작성 페이지로 이동
    }

    // 2-2. 게시글 작성
    @PostMapping("/newpost")
    public String insertBoard(@ModelAttribute PostDTO postDTO, @RequestParam(required = false) MultipartFile[] images) throws IOException {
        postService.insertPost(postDTO, List.of(images));

        System.out.println("[PostController] 컨트롤러 동작");
        return "redirect:/posts/" + postDTO.getId();
    }

    // 2-3. 게시글 수정




}
