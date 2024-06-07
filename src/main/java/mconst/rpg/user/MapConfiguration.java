package mconst.rpg.user;

import mconst.rpg.user.models.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfiguration {
    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }
}
