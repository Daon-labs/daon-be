package daon.be.kakao.controller;

import daon.be.kakao.dto.KakaoSkillRequest;
import daon.be.kakao.dto.KakaoSkillResponse;
import daon.be.kakao.service.KakaoWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoWebhookController {

    private final KakaoWebhookService kakaoWebhookService;

    @PostMapping("/api/kakao/webhook")
    public KakaoSkillResponse handleWebhook(@RequestBody KakaoSkillRequest request){
        return kakaoWebhookService.handle(request);
    }
}
