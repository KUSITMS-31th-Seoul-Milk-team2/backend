package com.seoulmilk.core.util.emailUtil.factory;

import com.seoulmilk.core.util.emailUtil.dto.SendEmailRequest;

public class SendEmailRequestFactory {
    private static final String TITLE = "서울우유 비밀번호 변경 안내";

    public static SendEmailRequest create(String receiver, String newPassword) {
        String content = "비밀번호가 변경되었습니다. 변경된 비밀번호는 " + newPassword + " 입니다.";
        return new SendEmailRequest(receiver, TITLE, content);
    }
}
