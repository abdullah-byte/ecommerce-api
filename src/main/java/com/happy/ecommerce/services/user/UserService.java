package com.happy.ecommerce.services.user;

import com.happy.ecommerce.dto.UserDto;
import com.happy.ecommerce.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(UserDto userDto);
}
