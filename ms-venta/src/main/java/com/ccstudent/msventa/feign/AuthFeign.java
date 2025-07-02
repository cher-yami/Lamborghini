package com.ccstudent.msventa.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="ms-auth-service")
public interface AuthFeign {
    @GetMapping("/auth/userId")
    ResponseEntity<Integer> getUserId(@RequestHeader("Authorization") String token);
    @GetMapping("/auth/userName")
    ResponseEntity<String> getUserName(@RequestHeader("Authorization") String token);
}

