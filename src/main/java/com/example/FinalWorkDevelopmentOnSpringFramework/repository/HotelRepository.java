package com.example.FinalWorkDevelopmentOnSpringFramework.repository;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
    select h from Hotel h
    where (:title is null or lower(h.title) like :title)
      and (:headingAdvertisements is null or lower(h.headingAdvertisements) like :headingAdvertisements)
      and (:city is null or lower(h.city) like :city)
      and (:address is null or lower(h.address) like :address)
      and (:distance is null or h.distance = :distance)
      and (:ratings is null or h.ratings = :ratings)
      and (:numberRatings is null or h.numberRatings = :numberRatings)
    """)
    List<Hotel> filterHotels(
            @Param("title") String title,
            @Param("headingAdvertisements") String headingAdvertisements,
            @Param("city") String city,
            @Param("address") String address,
            @Param("distance") Long distance,
            @Param("ratings") Long ratings,
            @Param("numberRatings") Long numberRatings
    );

}


