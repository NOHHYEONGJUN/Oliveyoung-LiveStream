package shop.olcl.backend.media.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.olcl.backend.media.dto.LiveStreamResponseDto;
import shop.olcl.backend.media.entity.LiveStreaming;
import shop.olcl.backend.media.repository.LiveStreamingRepository;

@RequiredArgsConstructor
@Service
public class LiveService {

    private final LiveStreamingRepository liveStreamingRepository;

    public List<LiveStreamResponseDto> getLiveStreams(LocalDateTime createdAt) {
        List<LiveStreaming> liveStreamingList = liveStreamingRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(
                createdAt);
        return liveStreamingList.stream()
                .map(liveStreaming -> new LiveStreamResponseDto(
                        liveStreaming.getId(),
                        liveStreaming.getLiveStreamingUrl(),
                        liveStreaming.getTitle(),
                        liveStreaming.getUpdatedAt(),
                        liveStreaming.getDeletedAt()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<LiveStreamResponseDto> getLiveStreamsPersonalized(LocalDateTime createdAt, String cognitoUserId) {
        List<LiveStreaming> liveStreamingList = liveStreamingRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(
                createdAt);
        return liveStreamingList.stream()
                .map(liveStreaming -> new LiveStreamResponseDto(
                        liveStreaming.getId(),
                        liveStreaming.getLiveStreamingUrl(),
                        liveStreaming.getTitle(),
                        liveStreaming.getUpdatedAt(),
                        liveStreaming.getDeletedAt()
                ))
                .collect(java.util.stream.Collectors.toList());
    }
}
