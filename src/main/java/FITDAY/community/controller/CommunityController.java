package FITDAY.community.controller;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommUpdateDto;
import FITDAY.community.dto.response.CommListDto;
import FITDAY.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/community")
@RequiredArgsConstructor
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/create")
    public ResponseEntity<CommUpdateDto> createCommunity(@RequestBody CommunityRequestDto requestDto) {
        return communityService.createCommunity(requestDto);
    }

    @PutMapping("/mod/{id}")
    public ResponseEntity<CommUpdateDto> updateCommunity(
            @PathVariable Long id,
            @RequestBody CommunityRequestDto requestDto) {
        return communityService.updateCommunity(id, requestDto);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deleteCommunity(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        communityService.deleteCommunity(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hot")
    public ResponseEntity<List<CommListDto>> getHotCommunity() {
        return communityService.getHotTopN();
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CommListDto>> getRecentCommunity() {
        return communityService.getRecentTopN();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<CommListDto>> getCommunityList(Pageable pageable) {
        return communityService.getCommunityList(pageable);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommUpdateDto> getCommunity(@PathVariable Long id) {
        return communityService.getCommunity(id);
    }
}
