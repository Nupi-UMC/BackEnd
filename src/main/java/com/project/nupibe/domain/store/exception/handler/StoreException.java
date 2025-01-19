package com.project.nupibe.domain.store.exception.handler;

import com.project.nupibe.global.apiPayload.code.BaseErrorCode;
import com.project.nupibe.global.apiPayload.exception.CustomException;


public class StoreException extends CustomException {
    public StoreException(BaseErrorCode code){super(code);}

}
