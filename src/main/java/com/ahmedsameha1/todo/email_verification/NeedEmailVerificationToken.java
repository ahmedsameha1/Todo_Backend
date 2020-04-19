package com.ahmedsameha1.todo.email_verification;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
public class NeedEmailVerificationToken extends ApplicationEvent {
    private UserAccount userAccount;
    private String appUrl;
    private Locale locale;

    public NeedEmailVerificationToken(UserAccount userAccount, String appUrl, Locale locale) {
        super(userAccount);
        this.userAccount = userAccount;
        this.appUrl = appUrl;
        this.locale = locale;
    }
}
