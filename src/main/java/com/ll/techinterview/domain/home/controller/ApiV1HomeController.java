package com.ll.techinterview.domain.home.controller;

import com.ll.techinterview.domain.home.dto.response.StatusResponse;
import com.ll.techinterview.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tech-interview/{spaceId}/home")
@RequiredArgsConstructor
public class ApiV1HomeController {
  private final HomeService homeService;

  @GetMapping()
  public ResponseEntity<StatusResponse> getStatus(
      @PathVariable("spaceId") Long spaceId
  ) {
    return ResponseEntity.ok(homeService.getStatus(spaceId));
  }
}
