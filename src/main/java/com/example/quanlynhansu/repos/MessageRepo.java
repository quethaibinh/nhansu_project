package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<MessageEntity, Long> {

//    cách này cũng được nhưng hơi khó mở rộng.
//    List<MessageEntity> findBySender_EmailAndReceiver_EmailOrReceiver_EmailAndSender_EmailOrderByTimestampAsc(
//            String s1, String r1, String s2, String r2);

    @Query("SELECT m FROM MessageEntity m " +
            "WHERE (m.sender.email = :email1 AND m.receiver.email = :email2) " +
            "   OR (m.sender.email = :email2 AND m.receiver.email = :email1) " +
            "ORDER BY m.createdAt ASC")
    List<MessageEntity> findChatHistoryBetween(@Param("email1") String email1,
                                               @Param("email2") String email2);


}

