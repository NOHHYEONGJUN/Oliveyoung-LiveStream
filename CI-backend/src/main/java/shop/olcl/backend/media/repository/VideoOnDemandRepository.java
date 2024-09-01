package shop.olcl.backend.media.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.olcl.backend.media.entity.VideoOnDemand;

@Repository
public interface VideoOnDemandRepository extends JpaRepository<VideoOnDemand, Long> {
    List<VideoOnDemand> findAllByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime createdAt);
}
