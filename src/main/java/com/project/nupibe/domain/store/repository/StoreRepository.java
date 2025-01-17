package com.project.nupibe.domain.store.repository;

import com.project.nupibe.domain.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s.id, s.name, s.image FROM RouteStore rs " +
            "JOIN rs.store s " +
            "WHERE rs.route.id = :routeId " +
            "ORDER BY rs.orderIndex ASC")
    List<Object[]> findStoresByRouteId(@Param("routeId") Long routeId);
}
