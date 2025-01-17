package com.project.nupibe.domain.store.repository;

import com.project.nupibe.domain.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepositroy extends JpaRepository<Store, Long> {
    @Query(
            value = "SELECT * FROM store s WHERE earth_distance(" +
                    "ll_to_earth(CAST(s.latitude as float), CAST(s.longitude as float))," +
                    "ll_to_earth(:latitude, :longitude)) < :radius",
            nativeQuery = true)
    Slice<Store> findByEarthDistance(Pageable pageable, @Param("latitude") Float latitude,
                                              @Param("longitude") Float longitude, @Param("radius") Integer radius);

    @Query(
            value = "SELECT * FROM store s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.group_info) LIKE LOWER(CONCAT('%', :query, '%'))",
            nativeQuery = true
    )
    Slice<Store> findByQuery(Pageable pageable, @Param("query") String query);


}
