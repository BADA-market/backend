package com.bada.Backend.domain.Chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
public class Chat {
    // 메시지  타입 : 입장, 채팅
    public enum MessageType{
        ENTER, TALK
    }
    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;
    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; // 채팅 발송 시간간

}