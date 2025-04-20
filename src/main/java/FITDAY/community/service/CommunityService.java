package FITDAY.community.service;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommunityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommunityService {

    ResponseEntity<CommunityResponseDto> createCommunity(CommunityRequestDto requestDto);

    ResponseEntity<CommunityResponseDto> updateCommunity(Long id, CommunityRequestDto requestDto);

    void deleteCommunity(Long id);

    ResponseEntity<List<CommunityResponseDto>> getHotTopN();

    ResponseEntity<List<CommunityResponseDto>> getRecentTopN();

    ResponseEntity<Page<CommunityResponseDto>> getCommunityList(Pageable pageable);
}
