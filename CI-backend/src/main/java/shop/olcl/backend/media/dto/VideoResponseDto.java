package shop.olcl.backend.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponseDto {
    private String vodUrl;
    private String videoId;
    private Long liveStreamingId;
}
