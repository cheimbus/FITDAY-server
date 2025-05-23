package FITDAY.community.service;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommPagingDto;
import FITDAY.community.dto.response.CommUpdateDto;
import FITDAY.community.dto.response.CommListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommunityService {

    ResponseEntity<CommUpdateDto> createCommunity(CommunityRequestDto requestDto);

    ResponseEntity<CommUpdateDto> updateCommunity(Long id, CommunityRequestDto requestDto);

    void deleteCommunity(Long id, String email);

    ResponseEntity<List<CommListDto>> getHotTopN();

    ResponseEntity<List<CommListDto>> getRecentTopN();

    ResponseEntity<Page<CommPagingDto>> getCommunityList(Pageable pageable);

    ResponseEntity<CommUpdateDto> getCommunity(Long id);
}
