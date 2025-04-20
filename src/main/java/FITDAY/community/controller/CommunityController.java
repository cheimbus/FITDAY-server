package FITDAY.community.controller;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommunityResponseDto;
import FITDAY.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/community")
@RequiredArgsConstructor
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/create")
    public ResponseEntity<CommunityResponseDto> createCommunity(@RequestBody CommunityRequestDto requestDto) {
        return communityService.createCommunity(requestDto);
    }

    @PutMapping("/mod/{id}")
    public ResponseEntity<CommunityResponseDto> updateCommunity(
            @PathVariable Long id,
            @RequestBody CommunityRequestDto requestDto) {
        return communityService.updateCommunity(id, requestDto);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hot")
    public ResponseEntity<List<CommunityResponseDto>> getHotCommunity() {
        return communityService.getHotTopN();
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CommunityResponseDto>> getRecentCommunity() {
        return communityService.getRecentTopN();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<CommunityResponseDto>> getCommunityList(Pageable pageable) {
        return communityService.getCommunityList(pageable);
    }
}
