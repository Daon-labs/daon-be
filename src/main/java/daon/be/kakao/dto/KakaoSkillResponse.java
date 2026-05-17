package daon.be.kakao.dto;

import java.util.List;


/**
 * kakao skill (SimpleText) 응답 예시
 * 현재 스킬 응답의 version은 2.0만 지원됩니다.
 *
 * {
 *     "version": "2.0",
 *     "template": {
 *         "outputs": [
 *             {
 *                 "simpleText": {
 *                     "text": "간단한 텍스트 요소입니다."
 *                 }
 *             }
 *         ]
 *     }
 * }
 *
 */
public record KakaoSkillResponse(
        String version,
        Template template
) {
    public static KakaoSkillResponse simpleText(String text) {
        return new KakaoSkillResponse(
                "2.0",
                new Template(List.of(
                        new Output(new SimpleText(text))
                ))
        );
    }

    public record Template(List<Output> outputs) {}
    public record Output(SimpleText simpleText) {}
    public record SimpleText(String text) {}
}
