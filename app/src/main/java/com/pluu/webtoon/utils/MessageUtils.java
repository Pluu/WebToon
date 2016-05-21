package com.pluu.webtoon.utils;

import android.content.Context;

import com.pluu.webtoon.R;
import com.pluu.webtoon.item.ERROR_TYPE;

/**
 * 메세지 유틸
 * Created by PLUUSYSTEM-NEW on 2016-05-22.
 */
public class MessageUtils {

    public static String getString(Context context, ERROR_TYPE errorType) {
        int resId;
        switch (errorType) {
            case ADULT_CERTIFY:
            case COINT_NEED:
                resId = R.string.msg_not_support;
                break;
            case NOT_SUPPORT:
                resId = R.string.not_support_type;
                break;
            default:
                resId = R.string.network_fail;
        }
        return context.getString(resId);
    }

}
