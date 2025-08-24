package com.matchmeeat.user.dto;

import java.util.List;

public record UserDto(
    String username,
    String email,
    List<String> roles
) {

}
