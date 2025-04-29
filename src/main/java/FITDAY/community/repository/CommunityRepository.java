package FITDAY.community.repository;

import FITDAY.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    long count();

    void deleteById(Long id);
}
