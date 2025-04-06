package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import org.springframework.data.repository.query.Param;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    @Modifying
    @Transactional
    @Query("delete from Message where messageId = :id")
    int deleteById(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("update Message set messageText = :text where messageId = :id")
    int updateMessageTextById(@Param("id") int id, @Param("text") String text);
}
