package com.project.nupibe.domain.store.repository;

import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository  extends JpaRepository<StoreImage, Long> {
}
