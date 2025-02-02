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
                   s.id, s.name, null, null, rs.orderIndex, null, s.image, s.latitude, s.longitude
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

    @Query("SELECT s.groupInfo FROM Store s")
    List<String> findAllGroup();

    @Query("SELECT DISTINCT s.category FROM Store s")
    List<String> findAllCategory();

    @Query("SELECT s FROM Store s ORDER BY ST_Distance(ST_MakePoint(s.longitude, s.latitude), ST_MakePoint(:longitude, :latitude))")
    List<Store> findAllOrderDistance(@Param("latitude") double latitude, @Param("longitude") double longitude);

    @Query("SELECT s FROM Store s ORDER BY s.bookmarkNum")
    List<Store> findAllOrderBookmark();

    @Query("SELECT s FROM Store s ORDER BY s.bookmarkNum")
    List<Store> findAllOrderRecommend();

    @Query("SELECT s FROM Store s WHERE s.category = :category ORDER BY ST_Distance(ST_MakePoint(s.longitude, s.latitude), ST_MakePoint(:longitude, :latitude))")
    List<Store> findCategoryOrderDistance(@Param("category") String category, @Param("latitude") double latitude, @Param("longitude") double longitude);

    @Query("SELECT s FROM Store s WHERE s.category = :category ORDER BY s.bookmarkNum")
    List<Store> findCategoryOrderBookmark(@Param("category") String category);

    @Query("SELECT s FROM Store s WHERE s.category = :category ORDER BY s.bookmarkNum")
    List<Store> findCategoryOrderRecommend(@Param("category") String category);

    @Query("SELECT s FROM Store s WHERE s.groupInfo = :groupName")
    List<Store> findByGroupName(@Param("groupName") String groupName);

    @Query("SELECT s FROM Store s WHERE s.region.id = :regionId ORDER BY ST_Distance(ST_MakePoint(s.longitude, s.latitude), ST_MakePoint(:longitude, :latitude))")
    List<Store> findAllOrderDistanceAndRegion(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("regionId") long regionId);

    @Query("SELECT s FROM Store s WHERE s.region.id = :regionId ORDER BY s.bookmarkNum")
    List<Store> findAllOrderBookmarkAndRegion(@Param("regionId") long regionId);

    @Query("SELECT s FROM Store s WHERE s.region.id = :regionId ORDER BY s.bookmarkNum")
    List<Store> findAllOrderRecommendAndRegion(@Param("regionId") long regionId);

    @Query("SELECT s FROM Store s WHERE s.category = :category AND s.region.id = :regionId ORDER BY ST_Distance(ST_MakePoint(s.longitude, s.latitude), ST_MakePoint(:longitude, :latitude))")
    List<Store> findCategoryOrderDistanceAndRegion(@Param("category") String category, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("regionId") long regionId);

    @Query("SELECT s FROM Store s WHERE s.category = :category AND s.region.id = :regionId ORDER BY s.bookmarkNum")
    List<Store> findCategoryOrderBookmarkAndRegion(@Param("category") String category, @Param("regionId") long regionId);

    @Query("SELECT s FROM Store s WHERE s.category = :category AND s.region.id = :regionId ORDER BY s.bookmarkNum")
    List<Store> findCategoryOrderRecommendAndRegion(@Param("category") String category, @Param("regionId") long regionId);
    @Query(
            value = "SELECT * FROM store s " +
                    "WHERE ST_DWithin(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius) " +
                    "ORDER BY ST_Distance(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC , s.id ASC",
            nativeQuery = true
    )
    Slice<Store> findAroundOrderByDistanceAscIdAsc(Pageable pageable, @Param("latitude") Float latitude,
                                                   @Param("longitude") Float longitude, @Param("radius") Integer radius);
    @Query(
            value = """
    SELECT s.*
    FROM store s
    WHERE ST_DWithin(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)
        AND (
            :cursor IS NULL OR 
            ST_Distance(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) > (
                SELECT ST_Distance(s3.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) 
                FROM store s3 
                WHERE s3.id = :cursor
            )
        )
    ORDER BY ST_Distance(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC , s.id ASC
""",
            nativeQuery = true
    )
    Slice<Store> findAroundOrderByDistanceWithCursor(@Param("storeId") Long cursor, Pageable pageable,
                                                     @Param("latitude") Float latitude,
                                                     @Param("longitude") Float longitude,
                                                     @Param("radius") Integer radius);

    @Query(
            value = "SELECT * FROM store s " +
                    "WHERE ST_DWithin(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius) " +
                    "ORDER BY s.bookmark_num DESC , s.id DESC ",
            nativeQuery = true
    )
    Slice<Store> findAroundOrderByBOOKMARKNUMAscIdAsc(Pageable pageable, @Param("latitude") Float latitude,
                                                      @Param("longitude") Float longitude, @Param("radius") Integer radius);
    @Query(value = """
        SELECT s.* FROM store s
        JOIN (
        SELECT s2.id AS cursor_id, CONCAT(
            LPAD(CAST(s2.bookmark_num AS TEXT), 10, '0'), 
            LPAD(CAST(s2.id AS TEXT), 10, '0')
        ) AS cursorValue
        FROM store s2
    ) AS cursorTable ON s.id = cursorTable.cursor_id
        WHERE ST_DWithin(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)
        AND cursorValue < (
            SELECT CONCAT(
                LPAD(CAST(s3.bookmark_num AS TEXT), 10, '0'), 
                LPAD(CAST(s3.id AS TEXT), 10, '0')
            ) 
            FROM store s3
            WHERE s3.id = :cursor
        )
    ORDER BY cursorTable.cursorValue DESC 
        """, nativeQuery = true)
    Slice<Store> findAroundOrderByBOOKMARKNUMWithCursor(@Param("storeId") Long cursor, Pageable pageable, @Param("latitude") Float latitude,
                                                        @Param("longitude") Float longitude, @Param("radius") Integer radius);

    @Query(
            value = """
                    SELECT * FROM store s
                    WHERE ST_DWithin(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)
                    ORDER BY s.like_num DESC , s.id DESC 
                    """,
            nativeQuery = true
    )
    Slice<Store> findAroundOrderByLikeNumAscIdAsc(Pageable pageable, @Param("latitude") Float latitude,
                                                  @Param("longitude") Float longitude, @Param("radius") Integer radius);
    @Query(value = """
        SELECT s.* FROM store s
        JOIN (
        SELECT s2.id AS cursor_id, CONCAT(
            LPAD(CAST(s2.like_num AS TEXT), 10, '0'), 
            LPAD(CAST(s2.id AS TEXT), 10, '0')
        ) AS cursorValue
        FROM store s2
    ) AS cursorTable ON s.id = cursorTable.cursor_id
        WHERE ST_DWithin(s.coordinates1, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)
        AND cursorValue < (
            SELECT CONCAT(
                LPAD(CAST(s3.like_num AS TEXT), 10, '0'), 
                LPAD(CAST(s3.id AS TEXT), 10, '0')
            ) 
            FROM store s3
            WHERE s3.id = :cursor
        )
    ORDER BY cursorTable.cursorValue DESC 
        """, nativeQuery = true)
    Slice<Store> findAroundOrderByLikeNumWithCursor(@Param("storeId") Long cursor, Pageable pageable, @Param("latitude") Float latitude,
                                                    @Param("longitude") Float longitude, @Param("radius") Integer radius);

    @Query(
            value = """
                    SELECT s.*, r.id AS r_id, r.name AS r_name 
                    FROM store s 
                    JOIN region r ON s.region_id = r.id
                    WHERE (r.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
                    LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%'))
                    ORDER BY ST_Distance(s.coordinates, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC, s.id ASC                                                                                                                            
                    """,
            nativeQuery = true
    )
    Slice<Store> findBySearchOrderByDistanceAscIdAsc(Pageable pageable, @Param("latitude") Float latitude,
                                                     @Param("longitude") Float longitude, @Param("search") String search);
    @Query(
            value = """
    SELECT s.*, r.id AS r_id, r.name AS r_name 
    FROM store s
    JOIN region r ON s.region_id = r.id
    WHERE 
        (
            (r.name LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%')))
        )
        AND (
            :cursor IS NULL OR 
            ST_Distance(s.coordinates, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) > (
                SELECT ST_Distance(s3.coordinates, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) 
                FROM store s3 
                WHERE s3.id = :cursor
            )
        )
    ORDER BY 
        ST_Distance(s.coordinates, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC
    """, nativeQuery = true)
    Slice<Store> findBySearchOrderByDistanceWithCursor(
            @Param("cursor") Long cursor,
            Pageable pageable,
            @Param("latitude") Float latitude,
            @Param("longitude") Float longitude,
            @Param("search") String search
    );

    @Query(
            value = """
                    SELECT s.*, r.id AS r_id, r.name AS r_name 
                    FROM store s
                    JOIN region r ON s.region_id = r.id
                    WHERE (r.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
                    LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%'))
                    ORDER BY s.bookmark_num DESC , s.id DESC 
                    """,
            nativeQuery = true
    )
    Slice<Store> findBySearchOrderByBOOKMARKNUMAscIdAsc(Pageable pageable, @Param("search") String search);
    @Query(value = """
    SELECT s.*, r.id AS r_id, r.name AS r_name, cursorTable.cursor_id FROM store s
    JOIN region r ON s.region_id = r.id 
    JOIN (
        SELECT s2.id AS cursor_id, CONCAT(
            LPAD(CAST(s2.bookmark_num AS TEXT), 10, '0'), 
            LPAD(CAST(s2.id AS TEXT), 10, '0')
        ) AS cursorValue
        FROM store s2
    ) AS cursorTable ON s.id = cursorTable.cursor_id
    WHERE (
        ((r.name LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%')))
        AND cursorValue < (
            SELECT CONCAT(
                LPAD(CAST(s3.bookmark_num AS TEXT), 10, '0'),
                LPAD(CAST(s3.id AS TEXT), 10, '0')
            )
            FROM store s3
            WHERE s3.id = :cursor
        ))
    )
    ORDER BY cursorTable.cursorValue DESC 
""", nativeQuery = true)
    Slice<Store> findBySearchOrderByBOOKMARKNUMWithCursor(@Param("storeId") Long cursor, Pageable pageable, @Param("search") String search);

    @Query(
            value = """
                    SELECT s.*, r.id AS r_id, r.name AS r_name 
                    FROM store s
                    JOIN region r ON s.region_id = r.id
                    WHERE (r.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
                    LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%'))
                    ORDER BY s.like_num DESC , s.id DESC 
                    """,
            nativeQuery = true
    )
    Slice<Store> findBySearchOrderByLikeNumAscIdAsc(Pageable pageable, @Param("search") String search);
    @Query(value = """
    SELECT s.*, r.id AS r_id, r.name AS r_name, cursorTable.cursor_id FROM store s
    JOIN region r ON s.region_id = r.id 
    JOIN (
        SELECT s2.id AS cursor_id, CONCAT(
            LPAD(CAST(s2.like_num AS TEXT), 10, '0'), 
            LPAD(CAST(s2.id AS TEXT), 10, '0')
        ) AS cursorValue
        FROM store s2
    ) AS cursorTable ON s.id = cursorTable.cursor_id
    WHERE (
        ((r.name LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%')))
        AND cursorValue < (
            SELECT CONCAT(
                LPAD(CAST(s3.like_num AS TEXT), 10, '0'),
                LPAD(CAST(s3.id AS TEXT), 10, '0')
            )
            FROM store s3
            WHERE s3.id = :cursor
        ))
    )
    ORDER BY cursorTable.cursorValue DESC 
""", nativeQuery = true)
    Slice<Store> findBySearchOrderByLikeNumWithCursor(@Param("cursor") Long cursor, Pageable pageable, @Param("search") String search);
}
