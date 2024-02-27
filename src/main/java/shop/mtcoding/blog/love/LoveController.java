package shop.mtcoding.blog.love;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.ApiUtil;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@RestController
public class LoveController {
    private final LoveRepository loveRepository;
    private final HttpSession session;

    @DeleteMapping("/api/love/{id}")
    public ApiUtil<?> delete(@PathVariable Integer id, HttpServletResponse response){
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            response.setStatus(401);
            return new ApiUtil<>(401, "인증안됨");
        }

        // 2. 권한 체크
        Love love = loveRepository.findById(id);
        if(love.getUserId() != sessionUser.getId()){
            response.setStatus(403);
            return new ApiUtil<>(403, "권한없음");
        }

        loveRepository.deleteById(id);
        return new ApiUtil<>(null);
    }

    @PostMapping("/api/love")
    public ApiUtil<?> save(@RequestBody LoveRequest.SaveDTO requestDTO, HttpServletResponse response){
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            response.setStatus(401);
            return new ApiUtil<>(401, "인증안됨");
        }

        Love love = loveRepository.save(requestDTO, sessionUser.getId());
        return new ApiUtil<>(love);
    }
}



