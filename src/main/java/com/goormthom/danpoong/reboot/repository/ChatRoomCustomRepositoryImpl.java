package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.QChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ChatRoom> findByUserAndChatType(User user, EChatType chatType) {
        QChatRoom chatRoom = QChatRoom.chatRoom;

        ChatRoom result = jpaQueryFactory.selectFrom(chatRoom)
                .where(
                        chatRoom.user.eq(user),
                        chatRoom.chatType.eq(chatType)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
