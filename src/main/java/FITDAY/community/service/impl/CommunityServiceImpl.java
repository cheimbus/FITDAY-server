package FITDAY.community.service.impl;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommunityResponseDto;
import FITDAY.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    @Override
    public ResponseEntity<CommunityResponseDto> createCommunity(CommunityRequestDto requestDto) {

        return null;
    }

    @Override
    public ResponseEntity<CommunityResponseDto> updateCommunity(Long id, CommunityRequestDto requestDto) {

        return null;
    }


    @Override
    public void deleteCommunity(Long id) {


    }

    @Override
    public ResponseEntity<List<CommunityResponseDto>> getHotTopN() {

        return null;
    }

    @Override
    public ResponseEntity<List<CommunityResponseDto>> getRecentTopN() {

        return null;
    }

    @Override
    public ResponseEntity<Page<CommunityResponseDto>> getCommunityList(Pageable pageable) {

        return null;
    }
}
