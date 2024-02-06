package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardRepository boardRepository;
    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);
        return "index";
    }
    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

        @GetMapping("/board/{id}")
        public String detail(@PathVariable int id, HttpServletRequest request) {
            System.out.println("id : "+id);

            // 바디 데이터가 없으면 유효성 검사가 필요없지 ㅎ
            BoardResponse.DetailDTO responseDTO = boardRepository.findById(id);

            request.setAttribute("board", responseDTO);
            return "board/detail";
        }
    }