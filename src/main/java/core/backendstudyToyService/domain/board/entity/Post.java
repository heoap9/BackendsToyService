package core.backendstudyToyService.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter @Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    public void addReply(Reply reply) {
        replyList.add(reply);
        reply.setPost(this);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setPost(this);
    }

    // 좋아요 수를 반환하는 메서드
    public int getLikeCount() {
        return likes.size();
    }
}
