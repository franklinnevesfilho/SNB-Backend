package com.groupfour.snb.repositories.listing;


import com.groupfour.snb.models.listing.attributes.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> { }
