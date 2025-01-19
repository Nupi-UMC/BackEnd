package com.project.nupibe.global.apiPayload.exception.handler;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import com.project.nupibe.global.apiPayload.exception.CustomException;

import java.security.GeneralSecurityException;

public class MemberHandler extends CustomException {
    public MemberHandler(BaseErrorCode errorCode){super(errorCode);}
}
