package com.encore.board.service;

import com.encore.board.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@Transactional
public class PostScheduler {

    private final PostRepository postRepository;

    @Autowired
    public PostScheduler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 초/분/시간/일/월/요일 형태로 스케줄링 설정
    // * : 매 초(분/시 등)의 항상을 의미
    // 특정 숫자 : 특정 숫자의 초(분/시 등)을 의미
    // 0/특정 숫자 : 특정 숫자마다
    // ex) 0 0 * * * * : 매일 0분 0초에 스케줄링 시작
    // ex) 0 0/1 * * * * : 매일 1분 마다 0초에 스케줄링 시작
    // ex) 0/1 * * * * * : 매 초마다
    // ex) 0 0 11 * * *: 매일 11시에 스케줄링
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void postSchedule(){
//        log.info("=====스케줄러 시작=====");
//        Page<Post> posts = postRepository.findByAppointment("Y", Pageable.unpaged());
//        for (Post post : posts.getContent()){
//            if (post.getAppointmentTime().isBefore(LocalDateTime.now())){
//                post.updateAppointment(null);
////                postRepository.save(post); // 더티 체킹돼서 굳이 필요 X
//            }
//        }
//        log.info("=====스케줄러 종료=====");
//    }
}