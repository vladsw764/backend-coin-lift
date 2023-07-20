package com.coinlift.communityservice.service.users;


import com.coinlift.userservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserRestService {
    @GetMapping("/api/v1/users/{userId}")
    UserResponseDto getUserById(@PathVariable("userId") UUID userId);

}

