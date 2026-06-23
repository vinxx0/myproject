package com.example.myproject;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.myproject.dto.AttachDTO;
import com.example.myproject.dto.BoardDTO;
import com.example.myproject.service.BoardService;
import org.springframework.data.domain.Sort;



// 단위 테스트 : 서버를 실행하지 않고 특정 위치의 코드를 
// 실행하여 테스트 진행 가능
@SpringBootTest // Test가 가능한 클래스로 컴퍼넌트 선언
public class BoardServiceTest {

    @Autowired // 선언된 클래스(인터페이스)의 인스턴스 주입
    private BoardService boardService;

    @Test
    void test() {
         saveBoard();
        // getBoardById();
        // getBoardsByPage();
    }


    private void getBoardsByPage() {
         Pageable pageable = PageRequest.of(0,2, Sort.by("boardId").descending());
           
        
       // of(pageNumber, pageSize) :
       // pageNumber(페이지 번호) : 0 -> 1페이지, 1 -> 2페이지
       // pageSize(페이지 크기) :10 -> 한 페이지 당 10개 
       // sort(정렬 방식) : Sort.by("boardId").descending()
       //                 -> boardId를 기준으로 내림차순 정렬

       Page<BoardDTO> list = boardService.getBoards(pageable);

       System.out.println("현재 페이지 : " + list.getNumber());
       System.out.println("페이지 크기 : " + list.getSize());
       System.out.println("전체 게시글 수 : " + list.getTotalElements());
       System.out.println("전체 페이지 수 : " + list.getTotalPages());

       // 각 데이터 출력
       list.getContent().forEach(System.out::println);
    }




    private void getBoardById() {
        BoardDTO boardDTO = boardService.getBoardById(1L);
        System.out.println(boardDTO);   
    }


   private void saveBoard() {
    // 1. 첨부 파일 리스트 생성
    List<AttachDTO> attachList = List.of(
        AttachDTO.builder()
                .filePath("/upload/2026/06/18/")
                .fileRealName("이미지파일.png")
                .fileChgName("uuid_image_01.png")
                .build(),

        AttachDTO.builder()
                .filePath("/upload/2026/06/18/")
                .fileRealName("문서파일.pdf")
                .fileChgName("uuid_doc_02.pdf")
                .build()
    );

    // 2. 게시글 DTO 생성 (첨부 파일 리스트 포함)
    BoardDTO boardDTO = BoardDTO.builder()
            .title("테스트 제목입니다")
            .content("테스트 내용입니다. 내용이 길어질 수 있습니다.")
            .writer("테스터")
            .regDate(LocalDateTime.now()) // 현재 시간 설정
            .commentCount(0)
            .attachCount(attachList.size()) // 첨부 파일 개수 자동 계산
            .attachList(attachList)
            .build();

    // 3. 서비스 호출
    boardService.saveBoard(boardDTO);

    System.out.println(boardDTO);
}

}

