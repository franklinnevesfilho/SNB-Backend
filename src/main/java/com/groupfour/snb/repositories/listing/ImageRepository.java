package com.groupfour.snb.repositories.listing;

import com.groupfour.snb.models.listing.attributes.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    Optional<Image> findByName(String name);
}
