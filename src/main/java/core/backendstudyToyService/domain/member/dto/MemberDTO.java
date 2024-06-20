package core.backendstudyToyService.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberDTO {

    @NotBlank(message = "input your name")
    private String username;


    @NotBlank(message = "input password")
    private String password;
}