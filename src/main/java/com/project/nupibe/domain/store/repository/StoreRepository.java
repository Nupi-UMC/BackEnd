package com.project.nupibe.domain.store.repository;

import com.project.nupibe.domain.route.dto.response.RouteStoreDTO;
import com.project.nupibe.domain.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("""
        SELECT new com.project.nupibe.domain.route.dto.response.RouteStoreDTO(
                   s.id, s.name, null, null, rs.orderIndex, null, s.image
               )
        FROM RouteStore rs
        JOIN rs.store s
        WHERE rs.route.id = :routeId
        ORDER BY rs.orderIndex ASC
    """)
    List<RouteStoreDTO> findStoresByRouteId(@Param("routeId") Long routeId);

    @Query("""
        SELECT new com.project.nupibe.domain.route.dto.response.RouteStoreDTO(
                   s.id, s.name, s.location, s.category, rs.orderIndex,
                   CASE
                       WHEN prev.store.coordinates IS NOT NULL THEN 
                           CAST(ST_DistanceSphere(prev.store.coordinates, s.coordinates) AS string)
                       ELSE '첫번째장소'
                   END,
                   s.image
               )
        FROM RouteStore rs
        JOIN rs.store s
        LEFT JOIN RouteStore prev ON prev.route.id = rs.route.id AND prev.orderIndex = rs.orderIndex - 1
        WHERE rs.route.id = :routeId
        ORDER BY rs.orderIndex ASC
    """)
    List<RouteStoreDTO> findStoresWithCalculatedDistance(@Param("routeId") Long routeId);
}
