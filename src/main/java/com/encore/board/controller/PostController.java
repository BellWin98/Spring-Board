package com.encore.board.controller;

import com.encore.board.dto.request.PostRequest;
import com.encore.board.dto.response.PostDetailsResponse;
import com.encore.board.dto.response.PostSimpleResponse;
import com.encore.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/posts")
@Controller
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
    public String write(PostRequest req){
        postService.write(req);
        return "redirect:/api/posts/post-list";
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

    @GetMapping("/post-list")
    public String showPostList(Model model){
        List<PostSimpleResponse> result = postService.showPostList();
        model.addAttribute("posts", result);

        return "post/post-list";
    }

    @GetMapping("detail/{id}")
    public String showPostDetails(@PathVariable Long id, Model model){
        PostDetailsResponse result = postService.showPostDetails(id);
        model.addAttribute("post", result);

        return "post/post-details";
    }
}
