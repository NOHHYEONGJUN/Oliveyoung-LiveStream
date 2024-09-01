package shop.olcl.backend.media.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.olcl.backend.media.entity.LiveStreaming;

@Repository
public interface LiveStreamingRepository extends JpaRepository<LiveStreaming, Long> {
    List<LiveStreaming> findAllByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime createdAt);
}
