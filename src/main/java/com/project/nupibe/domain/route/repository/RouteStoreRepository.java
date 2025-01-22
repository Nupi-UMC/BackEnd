package com.project.nupibe.domain.route.repository;

import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.entity.RouteStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RouteStoreRepository extends JpaRepository<RouteStore, Long> {
    @Query("SELECT rs.store.image FROM RouteStore rs WHERE rs.orderIndex = 0 and rs.route = :route")
    String findFirstImage(Route route);
}
