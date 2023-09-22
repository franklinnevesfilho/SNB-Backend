package com.groupfour.snb.models.services;

import com.groupfour.snb.models.listing.DTO.CreateListing;
import com.groupfour.snb.models.listing.attributes.Image;
import com.groupfour.snb.models.listing.Listing;
import com.groupfour.snb.models.listing.attributes.Message;
import com.groupfour.snb.models.user.User;
import com.groupfour.snb.repositories.listing.ImageRepository;
import com.groupfour.snb.repositories.listing.ListingRepository;
import com.groupfour.snb.repositories.listing.MessageRepository;
import com.groupfour.snb.utils.responses.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ListingService extends MainService {
    private final ListingRepository listingRepository;
    private final ImageRepository imageRepository;
    private final MessageRepository messageRepository;
    public Response addListing(CreateListing listing, String userId) {
        Listing listingCreated = listingRepository.save(Listing.builder()
                .user(User.builder().id(userId).build())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .build());
        return Response.builder()
                .node(mapToJson(listingCreated))
                .build();
    }
    public Response getListingWithId(String id){
        List<String> errors = new LinkedList<>();
        Optional<Listing> foundListing = listingRepository.findById(id);
        Listing listing = Listing.builder().build();
        if(foundListing.isPresent()){
            listing = foundListing.get();
        }
        return Response.builder()
                .node(mapToJson(listing))
                .errors(errors)
                .build();
    }
    public void addImages(String listingId , Set<Image> images){
        images.forEach(image -> image.setListing(listingRepository.findById(listingId).orElseThrow()));
        imageRepository.saveAll(images);
    }
    public Response addMessage(User user, String listingId, String message) {
        List<String> errors = new LinkedList<>();
        Optional<Listing> listing = listingRepository.findListingById(listingId);
        if(listing.isPresent()){
            Listing foundListing = listing.get();
            messageRepository.save(
                    Message.builder()
                            .user(user)
                            .listing(foundListing)
                            .message(message)
                            .build()
            );
        }else{
            errors.add("Listing with id: "+ listingId + " not found");
        }
        return Response.builder().node(mapToJson(message)).errors(errors).build();
    }
    public Response getAllListingsWithUser(String userId){
        return Response.builder()
                .node(mapToJson( listingRepository.findListingsByUserId(userId)))
                .build();
    }
}