package core.backendstudyToyService.domain.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingResponseDTO {

    private Object data;

    private PageDTO pageInfo;
}
