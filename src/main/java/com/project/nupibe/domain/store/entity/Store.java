package com.project.nupibe.domain.store.entity;

import com.project.nupibe.domain.region.entity.Region;
import com.project.nupibe.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "location")
    private String location; // 피그마 상 무슨 역 몇번 출구

    @Column(name = "address")//, nullable = false) test를 위해 nullable 삭제
    private String address;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "number")
    private String number;

    @Column(name = "sns_url")
    private String snsUrl;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "category", nullable = false)
    private String category; // 7개 카테고리 중 하나

    @Column(name = "group_info")
    private String groupInfo;

    @Column(name = "like_num", nullable = false)
    private int likeNum;

    @Column(name = "bookmark_num", nullable = false)
    private int bookmarkNum;

    @Column(name = "latitude", nullable = false)
    private float latitude;

    @Column(name = "longitude", nullable = false)
    private float longitude;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point coordinates; // PostGIS Point 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point coordinates1; // PostGIS Point 타입

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreImage> images;

    public void setBookmarkNum(int i) {
        this.bookmarkNum = i;
    }

    public void setLikeNum(int i) {
        this.likeNum = i;
    }
}
