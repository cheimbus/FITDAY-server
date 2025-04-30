package FITDAY.community.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityNotFoundException extends EntityNotFoundException {
    public CommunityNotFoundException(Long communityId) {
        super("communityId가 존재하지 않습니다. : " + communityId);
    }
}
