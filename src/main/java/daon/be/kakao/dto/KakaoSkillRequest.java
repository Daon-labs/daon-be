package daon.be.kakao.dto;

public record KakaoSkillRequest(
        UserRequest userRequest
) {
    public record UserRequest(
            String utterance,
            User user
    ) {
    }

    public record User(
            String id
    ) {
    }
}
