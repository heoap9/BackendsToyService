package core.backendstudyToyService;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.entity.Reply;
import core.backendstudyToyService.domain.board.entity.Like;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import core.backendstudyToyService.domain.board.repository.PostRepository;
import core.backendstudyToyService.domain.board.repository.ReplyRepository;
import core.backendstudyToyService.domain.board.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoryIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Member existingMember;

    @BeforeEach
    public void setup() {
        // 기존 회원 데이터 준비
        Member member = new Member();
        member.setUsername("existingUser");
        member.setPassword("existingPassword");
        existingMember = memberRepository.save(member);
    }

    @Test
    @DisplayName("게시글 작성후 좋아요와 댓글 생성 후 검증입니다")
    public void testMemberPostReplyLikeFlow() {
        // 게시글 생성 및 저장
        Post post = new Post();
        post.setTitle("테스트용 Post");
        post.setContent("이것은 게시글입니다.");
        post.setMember(existingMember);
        Post savedPost = postRepository.save(post);

        // 댓글 생성 및 저장
        Reply reply = new Reply();
        reply.setPost(savedPost);
        reply.setMember(existingMember);
        reply.setContent("이것은 답장입니다");
        Reply savedReply = replyRepository.save(reply);

        // 좋아요 생성 및 저장
        Like like = new Like();
        like.setPost(savedPost);
        like.setMember(existingMember);
        Like savedLike = likeRepository.save(like);

        // 검증
        List<Post> memberPosts = postRepository.findByMemberId(existingMember.getId());
        assertThat(memberPosts).hasSize(1).contains(savedPost);

        List<Reply> postReplies = replyRepository.findByPostId(savedPost.getId());
        assertThat(postReplies).hasSize(1).extracting(Reply::getContent).contains("이것은 답장입니다");

        List<Like> postLikes = likeRepository.findByPostId(savedPost.getId());
        assertThat(postLikes).hasSize(1);

        List<Reply> memberReplies = replyRepository.findByMemberId(existingMember.getId());
        assertThat(memberReplies).hasSize(1).extracting(Reply::getContent).contains("이것은 답장입니다");

        List<Like> memberLikes = likeRepository.findByMemberId(existingMember.getId());
        assertThat(memberLikes).hasSize(1);
    }

    @Test
    @DisplayName("회원 생성 후 테스트")
    public void testCreateMember() {
        Member newMember = new Member();
        newMember.setUsername("newUser");
        newMember.setPassword("newPassword");
        Member savedMember = memberRepository.save(newMember);

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isNotNull();
    }

    @Test
    @DisplayName("중복회원 테스트")
    public void testReadMember() {
        Optional<Member> foundMember = memberRepository.findById(existingMember.getId());

        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getUsername()).isEqualTo("existingUser");
    }

    @Test
    @DisplayName("회원정보 수정 테스트")
    public void testUpdateMember() {
        String updatedPassword = "updatedPassword";
        existingMember.setPassword(updatedPassword);
        Member updatedMember = memberRepository.save(existingMember);

        assertThat(updatedMember.getPassword()).isEqualTo(updatedPassword);
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    public void testDeleteMember() {
        boolean existsBeforeDelete = memberRepository.existsById(existingMember.getId());
        memberRepository.deleteById(existingMember.getId());
        boolean existsAfterDelete = memberRepository.existsById(existingMember.getId());

        assertThat(existsBeforeDelete).isTrue();
        assertThat(existsAfterDelete).isFalse();
    }
}
