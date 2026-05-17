package daon.be.kakao.dto;

import java.util.Map;
import java.util.Objects;

public record KakaoSkillRequest(
        UserRequest userRequest
) {
    public record UserRequest(
            String utterance,
            User user,
            String callbackUrl
    ) {
    }

    public record User(
            String id
    ) {
    }
}
