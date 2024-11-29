package com.example.cafemanagement.controller;

import com.example.cafemanagement.dto.CafeDto;
import com.example.cafemanagement.dto.CafeRequestDto;
import com.example.cafemanagement.dto.CategoryDto;
import com.example.cafemanagement.dto.LocationDto;
import com.example.cafemanagement.dto.MenuDto;
import com.example.cafemanagement.dto.ReviewDto;
import com.example.cafemanagement.dto.ReviewResponseDto;
import com.example.cafemanagement.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cafes")
@Tag(name = "카페 관리", description = "카페 추가, 조회, 검색 등 카페 관련 API")
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @PostMapping
    @Operation(summary = "카페 등록", description = "새로운 카페를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카페 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터")
    })
    public ResponseEntity<Long> addCafe(@RequestBody @Parameter(description = "카페 정보", required = true) CafeRequestDto requestDto) {
        Long cafeId = cafeService.addCafe(requestDto);
        return ResponseEntity.ok(cafeId);
    }

    @GetMapping("/{cafeId}")
    @Operation(summary = "카페 상세 조회", description = "특정 카페의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카페 상세 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카페를 찾을 수 없음")
    })
    public ResponseEntity<CafeDto> getCafeDetails(
            @PathVariable @Parameter(description = "카페 ID", required = true) Long cafeId) {
        CafeDto cafe = cafeService.getCafeDetails(cafeId);
        return ResponseEntity.ok(cafe);
    }

    //해시태그 수정
    @GetMapping("/search")
    @Operation(summary = "카페 검색", description = "키워드, 카테고리, 해시태그, 평점으로 카페를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 검색 조건")
    })
    public ResponseEntity<List<CafeDto>> searchCafes(
            @RequestParam(required = false) @Parameter(description = "검색 키워드") String keyword,
            @RequestParam(required = false) @Parameter(description = "카테고리 이름") String category,
            @RequestParam(required = false) @Parameter(description = "해시태그 이름") List<String> hashtags,
            @RequestParam(required = false) @Parameter(description = "최소 별점") Double minRating) {
        List<CafeDto> cafes = cafeService.searchCafes(keyword, category, hashtags, minRating);
        return ResponseEntity.ok(cafes);
    }

    @GetMapping("/categories")
    @Operation(summary = "카테고리 조회", description = "모든 카페 카테고리를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    })
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = cafeService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categoryName}")
    @Operation(summary = "카페 카테고리별 조회", description = "특정 카테고리의 카페를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카페 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    public ResponseEntity<List<CafeDto>> getCafesByCategory(
            @PathVariable @Parameter(description = "카테고리 이름", required = true) String categoryName) {
        List<CafeDto> cafes = cafeService.getCafesByCategory(categoryName);
        return ResponseEntity.ok(cafes);
    }

    @GetMapping("/popular")
    @Operation(summary = "추천 카페 조회", description = "평점 또는 조회수가 높은 인기 카페 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인기 카페 조회 성공")
    })
    public ResponseEntity<List<CafeDto>> getPopularCafes() {
        List<CafeDto> popularCafes = cafeService.getPopularCafes();
        return ResponseEntity.ok(popularCafes);
    }

    @PutMapping("/{cafeId}")
    @Operation(summary = "카페 정보 수정", description = "특정 카페의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카페 수정 성공"),
            @ApiResponse(responseCode = "404", description = "카페를 찾을 수 없음")
    })
    public ResponseEntity<Void> updateCafe(
            @PathVariable @Parameter(description = "수정할 카페 ID", required = true) Long cafeId,
            @RequestBody @Parameter(description = "수정할 카페 정보", required = true) CafeDto dto) {
        cafeService.updateCafe(cafeId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cafeId}")
    @Operation(summary = "카페 삭제", description = "특정 카페를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카페 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "카페를 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteCafe(
            @PathVariable @Parameter(description = "삭제할 카페 ID", required = true) Long cafeId) {
        cafeService.deleteCafe(cafeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/location")
    @Operation(summary = "위치 기반 카페 조회", description = "주어진 위치를 기준으로 카페를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 기반 카페 조회 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 위치 데이터")
    })
    public ResponseEntity<List<CafeDto>> findCafesByLocation(
            @RequestBody @Parameter(description = "위치 정보", required = true) LocationDto location) {
        List<CafeDto> cafes = cafeService.findCafesByLocation(location);
        return ResponseEntity.ok(cafes);
    }

    @GetMapping("/{cafeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getCafeReviews(@PathVariable Long cafeId) {
        List<ReviewResponseDto> reviews = cafeService.getCafeReviews(cafeId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{cafeId}/menus")
    public ResponseEntity<List<MenuDto>> getCafeMenus(@PathVariable Long cafeId) {
        List<MenuDto> menus = cafeService.getCafeMenus(cafeId);
        return ResponseEntity.ok(menus);
    }

}
