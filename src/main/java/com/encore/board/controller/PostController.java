package com.encore.board.controller;

import com.encore.board.dto.request.PostRequest;
import com.encore.board.dto.response.PostDetailsResponse;
import com.encore.board.dto.response.PostSimpleResponse;
import com.encore.board.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/posts")
@Controller
@Slf4j
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/write")
    public String write(){
        return "post/post-create";
    }
    @PostMapping("/write")
    public String write(PostRequest req, Model model){
        try{
            postService.write(req);
            return "redirect:/api/posts/post-list";
        } catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            log.error(e.getMessage());
            return "post/post-create";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, PostRequest req){
        postService.update(id, req);
        return "redirect:/api/posts/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        postService.delete(id);
        return "redirect:/api/posts/post-list";
    }

// 페이징 없는 게시글 목록 조회 API
//    @GetMapping("/post-list")
//    public String showPostList(Model model){
//        List<PostSimpleResponse> result = postService.showPostList();
//        model.addAttribute("posts", result);
//
//        return "post/post-list";
//    }

    @GetMapping("/post-list")
    // PageableDefault 값을 설정 안하면 page=0&size=20
    public String showPostPaging(Model model, @PageableDefault(size = 5, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostSimpleResponse> result = postService.showPostAppointmentPaging(pageable);
        model.addAttribute("posts", result);

        return "post/post-list";
    }

//    @GetMapping("/json/post-list")
//    @ResponseBody
//    // 쿼리 파라미터 방식으로 pageable 매개변수에 들어옴
//    // localhost:8080/api/posts/json/post-list?size=5&page=0&sort=createdTime,desc
//    public Page<PostSimpleResponse> showPostList(Pageable pageable){
//        return postService.showPostListJson(pageable);
//    }

    @GetMapping("detail/{id}")
    public String showPostDetails(@PathVariable Long id, Model model){
        PostDetailsResponse result = postService.showPostDetails(id);
        model.addAttribute("post", result);

        return "post/post-details";
    }
}
