package core.backendstudyToyService;

import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.entity.Reply;
import core.backendstudyToyService.domain.board.repository.PostRepository;
import core.backendstudyToyService.domain.board.service.PostService;
import core.backendstudyToyService.domain.member.entitiy.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @Test
    public void getPostDetails_WithValidPost_ReturnsDetails() {
        // Arrange
        Long postId = 1L;
        Member member = new Member();
        member.setUsername("햄스터");

        Post post = new Post();
        post.setId(postId);
        post.setTitle("샘플제목");
        post.setContent("이것은 내용입니다");
        post.setMember(member);

        Reply reply = new Reply();
        reply.setMember(member);
        reply.setContent("굉장한 글이에요!");
        post.addReply(reply);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        PostDetailsDTO result = postService.getPostDetails(postId);

        // Assert
        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals("샘플제목", result.getTitle());
        assertEquals("이것은 내용입니다.", result.getContent());
        assertEquals("햄스터", result.getAuthorName());
        assertEquals(0, result.getLikeCount()); // Assumes no likes added in the setup
        assertEquals(1, result.getComments().size());
        assertEquals("굉장한 글이에요!!", result.getComments().get(0).getContent());
    }

}
