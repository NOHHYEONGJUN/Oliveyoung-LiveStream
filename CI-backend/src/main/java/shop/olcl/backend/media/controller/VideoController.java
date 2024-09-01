package shop.olcl.backend.media.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.olcl.backend.common.exception.UserNotFoundException;
import shop.olcl.backend.media.dto.VideoResponseDto;
import shop.olcl.backend.media.service.VideoService;

@RequiredArgsConstructor
@RestController
public class VideoController {

    private final VideoService videoService;

    @GetMapping(value = "/media/video/latest")
    public ResponseEntity<?> getVideoOnDemands() {
        LocalDateTime createdAt = LocalDateTime.now().minusMonths(1);
        List<VideoResponseDto> videos = videoService.getVideoOnDemandsSortedByNewest(createdAt);
        return ResponseEntity.ok().body(videos);
    }

    @GetMapping(value = "/media/video/personalized")
    public ResponseEntity<?> getVideoOnDemandsPersonalized(@AuthenticationPrincipal Jwt jwt) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cognitoUserId = authentication.getName();
        LocalDateTime createdAt = LocalDateTime.now().minusMonths(1);
        try {
            List<VideoResponseDto> videos = videoService.getVideoOnDemandSortedByPersonalRecommendation(createdAt,
                    cognitoUserId);
            return ResponseEntity.ok().body(videos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
