package shop.olcl.backend.media.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.olcl.backend.common.exception.UserNotFoundException;
import shop.olcl.backend.media.dto.VideoResponseDto;
import shop.olcl.backend.media.entity.VideoOnDemand;
import shop.olcl.backend.media.repository.VideoOnDemandRepository;
import shop.olcl.backend.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class VideoService {

    private final PersonalizeRecommendationService personalizeRecommendationService;
    private final VideoOnDemandRepository videoOnDemandRepository;
    private final UserRepository userRepository;

    @Value("${aws.personalize.campaign-arn}")
    private String campaignArn;

    public List<VideoResponseDto> getVideoOnDemandsSortedByNewest(LocalDateTime startDate) {
        List<VideoOnDemand> videoList = videoOnDemandRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(startDate);
        return getVideoResponseDtoList(videoList);
    }

    @SneakyThrows
    public List<VideoResponseDto> getVideoOnDemandSortedByPersonalRecommendation(LocalDateTime startDate,
                                                                                 String cognitoUserId) {
        List<VideoOnDemand> videoList = videoOnDemandRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(startDate);
        userRepository.findById(UUID.fromString(cognitoUserId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + cognitoUserId));

        List<String> items = videoList.stream()
                .map(VideoOnDemand::getVideoId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<String> personalizedRanking = personalizeRecommendationService.getPersonalizedRanking(campaignArn,
                cognitoUserId, items);

        List<VideoOnDemand> rankedVideoList = videoList.stream()
                .filter(video -> personalizedRanking.contains(video.getVideoId()))
                .sorted(Comparator.comparingInt(video -> personalizedRanking.indexOf(video.getVideoId())))
                .collect(Collectors.toList());

        return getVideoResponseDtoList(rankedVideoList);
    }

    private static List<VideoResponseDto> getVideoResponseDtoList(List<VideoOnDemand> videoList) {
        return videoList.stream()
                .filter(video -> video.getVodUrl() != null && video.getVideoId() != null)
                .map(video -> new VideoResponseDto(video.getVodUrl(), video.getVideoId(), video.getLiveStreamingId()))
                .distinct().toList();
    }
}