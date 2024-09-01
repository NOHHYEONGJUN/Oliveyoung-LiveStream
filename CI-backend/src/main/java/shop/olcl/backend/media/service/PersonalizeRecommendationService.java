package shop.olcl.backend.media.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import shop.olcl.backend.media.dto.VideoResponseDto;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;
import software.amazon.awssdk.services.personalizeruntime.model.GetPersonalizedRankingRequest;
import software.amazon.awssdk.services.personalizeruntime.model.GetPersonalizedRankingResponse;
import software.amazon.awssdk.services.personalizeruntime.model.PredictedItem;

@RequiredArgsConstructor
@Service
public class PersonalizeRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(PersonalizeRecommendationService.class);
    private final PersonalizeRuntimeClient personalizeRuntimeClient;

    public List<String> getPersonalizedRanking(String campaignArn, String userId, List<String> inputList) {
        try {
            log.atInfo().log("Getting personalized ranking for user: {}", userId);
            log.atInfo().log("inputList: {}", inputList);
            GetPersonalizedRankingRequest rankingRequest = GetPersonalizedRankingRequest.builder()
                    .campaignArn(campaignArn)
                    .userId(userId)
                    .inputList(inputList)  // 이 리스트는 재정렬할 아이템 ID 목록입니다.
                    .build();

            GetPersonalizedRankingResponse rankingResponse = personalizeRuntimeClient
                    .getPersonalizedRanking(rankingRequest);

            return rankingResponse.personalizedRanking().stream()
                    .map(PredictedItem::itemId)
                    .toList();
        } catch (AwsServiceException e) {
            log.error("Failed to get personalized ranking: {}", e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
