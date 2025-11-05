package com.example.FinalWorkDevelopmentOnSpringFramework.repository;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = """
    SELECT r.* FROM room r
    WHERE (:description IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))
      AND (:minPrice IS NULL OR r.price >= :minPrice)
      AND (:maxPrice IS NULL OR r.price <= :maxPrice)
      AND (:maximumPeople IS NULL OR r.maximum_people = :maximumPeople)
      AND (:roomId IS NULL OR r.id = :roomId)
      AND (
           CAST(:dateCheckIn AS date) IS NULL
        OR CAST(:dateCheckOut AS date) IS NULL
        OR r.unavailable_begin IS NULL
        OR r.unavailable_end IS NULL
        OR r.unavailable_begin > CAST(:dateCheckOut AS date)
        OR r.unavailable_end < CAST(:dateCheckIn AS date)
      )
    """,
            countQuery = """
    SELECT count(*) FROM room r
    WHERE (:description IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))
      AND (:minPrice IS NULL OR r.price >= :minPrice)
      AND (:maxPrice IS NULL OR r.price <= :maxPrice)
      AND (:maximumPeople IS NULL OR r.maximum_people = :maximumPeople)
      AND (:roomId IS NULL OR r.id = :roomId)
      AND (
           CAST(:dateCheckIn AS date) IS NULL
        OR CAST(:dateCheckOut AS date) IS NULL
        OR r.unavailable_begin IS NULL
        OR r.unavailable_end IS NULL
        OR r.unavailable_begin > CAST(:dateCheckOut AS date)
        OR r.unavailable_end < CAST(:dateCheckIn AS date)
      )
    """,
            nativeQuery = true
    )
    Page<Room> findFilter(
            @Param("description") String description,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("maximumPeople") Long maximumPeople,
            @Param("dateCheckIn") LocalDate dateCheckIn,
            @Param("dateCheckOut") LocalDate dateCheckOut,
            @Param("roomId") Long roomId,
            Pageable pageable
    );
}
