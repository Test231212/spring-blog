package shop.mtcoding.blog.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
    }
}
