package daon.be.kakao.dto;

import java.util.Map;

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
            Map<String, String> properties
    ) {
    }
}
