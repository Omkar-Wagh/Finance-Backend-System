package com.finance.service.user;

import com.finance.dto.UserDto;
import com.finance.dto.UserLoginRequest;
import com.finance.dto.UserRequestDto;

public interface UserService {
    UserDto register(UserRequestDto dto);

    String login(UserLoginRequest dto);

    UserDto update(UserDto dto);
}
