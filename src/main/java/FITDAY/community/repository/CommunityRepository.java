package FITDAY.community.repository;

import FITDAY.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    long count();

    void deleteById(Long id);

    @Query("SELECT c.readCnt FROM Community c WHERE c.id =:id")
    long findReadCntById(Long id);

    @Modifying
    @Query("UPDATE Community c SET c.readCnt = c.readCnt + :delta WHERE c.id = :id")
    int incrementReadCount(@Param("id") Long id, @Param("delta") long delta);
}
