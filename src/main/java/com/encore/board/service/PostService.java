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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public void write(PostRequest req, String email) throws IllegalArgumentException{
        // SecurityContextHolder 계층 구조에 의거, 서비스단에서도 Authentication 객체를 가져올 수 있다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElse(null);
        LocalDateTime reservationTime = null;
        String appointment = null;
        if (req.getAppointment().equals("Y") && !req.getAppointmentTime().isEmpty()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            reservationTime = LocalDateTime.parse(req.getAppointmentTime(), dateTimeFormatter);
            LocalDateTime now = LocalDateTime.now();
            if (reservationTime.isBefore(now)){
                throw new IllegalArgumentException("입력한 예약 발송 시간이 현재 시간보다 빠릅니다.");
            }
            appointment = "Y";
        }
        Post post = Post.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .member(member)
                .appointment(appointment)
                .appointmentTime(reservationTime)
                .build();

        // 더티 체킹 테스트 (글을 작성한 Member의 이름을 변경해보기)
//        member.updateMemberInfo("1356", "Dirty Checking Test");
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

//    // 페이지네이션 없는 게시글 목록 조회
//    public List<PostSimpleResponse> showPostList(){
//        List<Post> posts = postRepository.findAllFetchJoin();
//        List<PostSimpleResponse> responses = new ArrayList<>();
//        for (Post post : posts){
//            PostSimpleResponse postSimpleResponse = PostSimpleResponse.builder()
//                    .id(post.getId())
//                    .title(post.getTitle())
//                    .memberEmail(post.getMember() == null ? "익명": post.getMember().getEmail())
//                    .build();
//            responses.add(postSimpleResponse);
//        }
//        return responses;
//    }

    public Page<PostSimpleResponse> showPostPaging(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        // Page 객체에서 map을 지원. p(post)를 하나씩 꺼내서 DTO 형태에 맞게 세팅
        return posts.map(p -> PostSimpleResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .memberEmail(p.getMember() == null ? "익명": p.getMember().getEmail())
                .build());
    }

    public Page<PostSimpleResponse> showPostAppointmentPaging(Pageable pageable){
        Page<Post> posts = postRepository.findByAppointment(null, pageable);
        return posts.map(p -> PostSimpleResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .memberEmail(p.getMember() == null ? "익명": p.getMember().getEmail())
                .build());
    }

//    public Page<PostSimpleResponse> showPostListJson(Pageable pageable){
//        Page<Post> posts = postRepository.findAll(pageable);
//        // Page 객체에서 map을 지원. p(post)를 하나씩 꺼내서 DTO 형태에 맞게 세팅
//        return posts.map(p -> PostSimpleResponse.builder()
//                .id(p.getId())
//                .title(p.getTitle())
//                .memberEmail(p.getMember() == null ? "익명": p.getMember().getEmail())
//                .build());
//    }

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
