package com.project.nupibe.domain.route.exception;

import com.project.nupibe.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class RouteException extends CustomException {

    public RouteException(RouteErrorCode errorCode){
        super(errorCode);
    }
}
