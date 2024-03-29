package jipdol2.eunstargram.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SessionResponseDTO {

    private String accessToken;

    public SessionResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
