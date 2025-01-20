package com.project.nupibe.domain.store.repository;

import com.project.nupibe.domain.route.dto.response.RouteStoreDTO;
import com.project.nupibe.domain.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @Query(value = """
    SELECT 
        s1.id AS storeId,
        s1.name AS name,
        s1.location AS location,
        s1.category AS category,
        rs1.order_index AS orderIndex,
        CASE
            WHEN s2.coordinates IS NOT NULL THEN
                ST_DistanceSphere(s2.coordinates, s1.coordinates)
            ELSE NULL
        END AS distance,
        s1.image AS image
    FROM route_store rs1
    JOIN store s1 ON rs1.store_id = s1.id
    LEFT JOIN route_store rs2 ON rs2.route_id = rs1.route_id AND rs2.order_index = rs1.order_index - 1
    LEFT JOIN store s2 ON rs2.store_id = s2.id
    WHERE rs1.route_id = :routeId
    ORDER BY rs1.order_index
""", nativeQuery = true)
    List<Object[]> findStoresWithCalculatedDistance(@Param("routeId") Long routeId);

    @Query("SELECT s.group FROM Store s")
    List<String> findAllGroup();
}
