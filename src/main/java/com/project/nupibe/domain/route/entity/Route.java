package com.project.nupibe.domain.route.entity;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "route")
public class Route extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "like_num", nullable = false)
    private int likeNum;

    @Column(name = "bookmark_num", nullable = false)
    private int bookmarkNum;

    @Column(name="category", nullable = false)
    private String category;
}
