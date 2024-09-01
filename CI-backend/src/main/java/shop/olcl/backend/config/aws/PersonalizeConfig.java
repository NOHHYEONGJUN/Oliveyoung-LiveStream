package shop.olcl.backend.config.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;

@Configuration
public class PersonalizeConfig {

    @Bean
    public PersonalizeRuntimeClient personalizeRuntimeClient() {
        return PersonalizeRuntimeClient.builder()
                .region(Region.of("ap-northeast-2")) // 적절한 리전으로 변경하세요
                .build();
    }
}
