package shop.olcl.backend.media.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.olcl.backend.common.entity.BaseTimeEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "live_streaming")
public class LiveStreaming extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "live_streaming_url", nullable = false)
    private String liveStreamingUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime deletedAt;
}
