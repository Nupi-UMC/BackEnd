package com.project.nupibe.domain.member.exception.handler;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import com.project.nupibe.global.apiPayload.exception.CustomException;

public class MemberException  extends CustomException {
    public MemberException(BaseErrorCode code){super(code);}

}
