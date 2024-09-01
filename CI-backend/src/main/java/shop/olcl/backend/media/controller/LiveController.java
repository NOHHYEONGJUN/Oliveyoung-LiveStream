package shop.olcl.backend.media.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.olcl.backend.media.service.LiveService;

@RequiredArgsConstructor
@RestController
public class LiveController {

    private final LiveService liveService;

    @GetMapping(value = "/media/live/latest")
    public ResponseEntity<?> getLiveStreams() {
        LocalDateTime createdAt = LocalDateTime.now().minusMonths(1);
        return ResponseEntity.ok(liveService.getLiveStreams(createdAt));
    }

    @GetMapping(value = "/media/live/personalized")
    public ResponseEntity<?> getLiveStreamsPersonalized(@AuthenticationPrincipal Jwt jwt) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cognitoUserId = authentication.getName();
        LocalDateTime createdAt = LocalDateTime.now().minusMonths(1);
        return ResponseEntity.ok(liveService.getLiveStreamsPersonalized(createdAt, cognitoUserId));
    }
}
