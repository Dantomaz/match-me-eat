package com.matchmeeat.utils;

import com.matchmeeat.auth.dto.RegistrationRequestDto;
import com.matchmeeat.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserUtils {

    public static boolean userProvidedEmail(RegistrationRequestDto user) {
        return StringUtils.isNoneBlank(user.email());
    }

    public static boolean userProvidedEmail(User user) {
        return StringUtils.isNoneBlank(user.getEmail());
    }
}
