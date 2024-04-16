package core.backendstudyToyService.domain.member.entitiy;

import core.backendstudyToyService.domain.board.entity.Like;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.entity.Reply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reply> replyList = new ArrayList<>();

}

