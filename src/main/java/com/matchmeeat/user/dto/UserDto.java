package com.matchmeeat.user.dto;

import com.matchmeeat.annotations.Default;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
// @Default is my custom annotation. MapStruct uses ANY @Default annotation to mark preferred constructor used for mapping to resolve ambiguity.
@AllArgsConstructor(onConstructor_ = @Default)
public final class UserDto {

    private final String username;
    private final String email;
    private final List<String> roles;

    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
        this.roles = new ArrayList<>();
    }
}
