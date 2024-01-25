package com.encore.board.service;

import com.encore.board.dto.request.PostRequest;
import com.encore.board.dto.response.PostDetailsResponse;
import com.encore.board.dto.response.PostSimpleResponse;
import com.encore.board.entity.Member;
import com.encore.board.entity.Post;
import com.encore.board.exception.post.PostNotExistException;
import com.encore.board.repository.MemberRepository;
import com.encore.board.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PostService(PostRepository postRepository, MemberRepository memberRepository){
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void write(PostRequest req){
        Member member = memberRepository.findByEmail(req.getEmail()).orElse(null);
        Post post = Post.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .member(member)
                .build();

        // 더티 체킹 테스트 (글을 작성한 Member의 이름을 변경해보기)
        member.updateMemberInfo("1356", "Dirty Checking Test");
        postRepository.save(post);
    }

    @Transactional
    public void update(Long id, PostRequest req){
        Post post = postRepository.findById(id).orElseThrow(PostNotExistException::new);
        post.updateTitleContents(req.getTitle(), req.getContents());
//        postRepository.save(post);
    }

    public void delete(Long id){
        Post post = postRepository.findById(id).orElseThrow(PostNotExistException::new);
        postRepository.delete(post);
    }

    public List<PostSimpleResponse> showPostList(){
        List<Post> posts = postRepository.findAllFetchJoin();
        List<PostSimpleResponse> responses = new ArrayList<>();
        for (Post post : posts){
            PostSimpleResponse postSimpleResponse = PostSimpleResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .memberEmail(post.getMember() == null ? "익명": post.getMember().getEmail())
                    .build();
            responses.add(postSimpleResponse);
        }
        return responses;
    }

    public PostDetailsResponse showPostDetails(Long id){
        Post post = postRepository.findById(id).orElseThrow(PostNotExistException::new);

        return PostDetailsResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdTime(post.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
