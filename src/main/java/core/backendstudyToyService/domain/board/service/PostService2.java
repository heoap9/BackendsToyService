package core.backendstudyToyService.domain.board.service;

import core.backendstudyToyService.domain.board.dto.PostDTO;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.repository.PostRepository;
import core.backendstudyToyService.domain.common.Criteria;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;


@Service
public class PostService2 {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostService2(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public int selectPostTotal() {
        List<Post> postList = postRepository.findAll();

        System.out.println("갯수 : " + postList.size());
        return postList.size();
    }

    public Object selectPostListWithPaging(Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("uploadDate").descending());

        Page<Post> result = postRepository.findAll(paging); // 모든 게시물 조회

        if (result.isEmpty()) {
            return "게시물이 없습니다.";
        }

        return result;
    }

    @Transactional
    public void insertPost(PostDTO postDTO, List<MultipartFile> images) {
//        java.util.Date utilDate = new java.util.Date();
//        long currentMilliseconds = utilDate.getTime();
//        java.sql.Date sqlDate = new java.sql.Date(currentMilliseconds);
        Date currentDate = new Date(System.currentTimeMillis());

        Post post = modelMapper.map(postDTO, Post.class);
        postDTO.setUploadDate(currentDate);
        postDTO.setModifiedDate(currentDate);
        postDTO.setIsDeleted('N');

        postRepository.save(post);
        System.out.println("[PostService] 게시물 등록 성공!");
    }

}
