package core.backendstudyToyService.domain.board.entity;

import core.backendstudyToyService.domain.member.entitiy.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title") // 제목
    private String title;

    @Column(name = "content") // 내용
    private String content;

    @Column(name = "upload_Date") // 업로드 날짜
    private LocalDateTime uploadDate;

    @Column(name = "modified_Date") // 수정 날짜
    private LocalDateTime modifiedDate;

    @Column(name = "isDeleted") // 삭제 여부
    private char isDeleted;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // 이미지 파일
    private List<PostImage> images = new ArrayList<>();

//    @Column(name = "allow_comment") // 댓글 허용 여부
//    private String allow_comment;

//    @Column(name = "views") // 조회수
//    private int views;

    // 댓글
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    // 좋아요
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    public void addReply(Reply reply) {
        replyList.add(reply);
        reply.setPost(this);
    }

    // 좋아요 수를 반환하는 메서드
    public int getLikeCount() {
        return likes.size();
    }

}
