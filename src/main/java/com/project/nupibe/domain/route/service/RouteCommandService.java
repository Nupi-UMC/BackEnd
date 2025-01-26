package com.project.nupibe.domain.route.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberRoute;
import com.project.nupibe.domain.member.entity.MemberStore;
import com.project.nupibe.domain.member.entity.RouteLike;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberRouteRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.member.repository.RouteLikeRepository;
import com.project.nupibe.domain.route.converter.RouteConverter;
import com.project.nupibe.domain.route.dto.response.RouteDetailResDTO;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.exception.RouteErrorCode;
import com.project.nupibe.domain.route.exception.RouteException;
import com.project.nupibe.domain.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteCommandService {

    private final MemberRepository memberRepository;
    private final RouteRepository routeRepository;
    private final MemberRouteRepository memberRouteRepository;
    private final RouteLikeRepository routeLikeRepository;

    public RouteDetailResDTO.savedDTO bookmarkRoute(Long memberId, Long routeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        boolean exists = memberRouteRepository.existsByMemberIdAndRouteId(memberId, routeId);
        //존재하는지 확인
        if (exists) {
            //route 조회 후 삭제
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));
            MemberRoute memberRoute = memberRouteRepository.findByMemberandRoute(member, route);
            memberRouteRepository.delete(memberRoute);

            // route의 북마크 수 감소
            route.setBookmarkNum(route.getBookmarkNum() - 1);
            routeRepository.save(route);

        }
        else{
            //route 조회 후 추가
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));
            MemberRoute memberRoute = MemberRoute.builder()
                    .member(member)
                    .route(route)
                    .build();
            memberRouteRepository.save(memberRoute);

            // route의 북마크 수 증가
            route.setBookmarkNum(route.getBookmarkNum() + 1);
            routeRepository.save(route);
        }
        return RouteConverter.save(routeId,!exists);
    }

    public RouteDetailResDTO.savedDTO likeRoute(Long memberId, Long routeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        boolean exists = routeLikeRepository.existsByMemberIdAndRouteId(memberId, routeId);
        //존재하는지 확인
        if (exists) {
            //route 조회 후 삭제
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));
            RouteLike routeLike = routeLikeRepository.findByMemberandRoute(member, route);
            routeLikeRepository.delete(routeLike);

            // route의 좋아요 수 감소
            route.setLikeNum(route.getLikeNum() - 1);
            routeRepository.save(route);

        }
        else{
            //route 조회 후 추가
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));
            RouteLike routeLike = RouteLike.builder()
                    .member(member)
                    .route(route)
                    .build();
            routeLikeRepository.save(routeLike);

            // route의 북마크 수 증가
            route.setLikeNum(route.getLikeNum() + 1);
            routeRepository.save(route);
        }
        return RouteConverter.save(routeId,!exists);
    }
}
