package com.project.nupibe.global.apiPayload.exception.handler;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import com.project.nupibe.global.apiPayload.exception.CustomException;

public class StoreHandler extends CustomException {
    public StoreHandler(BaseErrorCode errorCode){super(errorCode);}
}