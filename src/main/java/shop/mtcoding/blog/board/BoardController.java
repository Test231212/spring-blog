package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.config.security.MyLoginUser;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;
import shop.mtcoding.blog.user.UserRequest;

import java.util.HashMap;
import java.util.List;
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final HttpSession session;
    private final BoardRepository boardRepository;


        // ?title=제목1&content=내용1
    // title=제목1&content=내용1
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO, @AuthenticationPrincipal MyLoginUser myLoginUser){

       // 권한 체크
        Board board = boardRepository.findById(id);
        if (board.getUserId() != myLoginUser.getUser().getId()) {
            return "error/403";
        }
       // 핵심 로직
       // update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/"+id;
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser){

        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);

        // 권한 체크
        if (board.getUserId() != myLoginUser.getUser().getId()) {

            return "error/403";
        }

        // 가방에 담기
        request.setAttribute("board", board);

        return "board/updateForm";
    }


    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {

        // 2. 권한 없으면 나가
        Board board = boardRepository.findById(id);
        if (board.getUserId() != myLoginUser.getUser().getId()) {
            request.setAttribute("status", 403);
            request.setAttribute("msg", "게시글을 삭제할 권한이 없습니다");
            return "error/40x";
        }

        boardRepository.deleteById(id);

        return "redirect:/";
    }


    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        // 2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        // 3. 모델 위임
        // insert into board_tb(title, content, user_id, created_at) values(?,?,?, now());
        boardRepository.save(requestDTO, myLoginUser.getUser().getId());

        return "redirect:/";
    }


    @GetMapping("/")
    public String index(HttpServletRequest request) {



        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);
        return "index";
    }
    //   /board/saveForm 요청(Get)이 온다
    @GetMapping("/board/saveForm")
    public String saveForm() {
            return "board/saveForm";
        }


    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        // 1. 모델 진입 - 상세보기 데이터 가져오기
        BoardResponse.DetailDTO responseDTO = boardRepository.findByIdWithUser(id);

        boolean pageOwner;
        if (myLoginUser == null) {
            pageOwner = false;
        } else {
            int 게시글작성자번호 = responseDTO.getUserId();
            int 로그인한사람의번호 = myLoginUser.getUser().getId();
            pageOwner = 게시글작성자번호 == 로그인한사람의번호;
        }

        request.setAttribute("board", responseDTO);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail";
    }
}