package com.project.nupibe.domain.store.entity;

import com.project.nupibe.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
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

    @Column(name = "address", nullable = false)
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
}
