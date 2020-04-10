package com.ahmedsameha1.todo.email_verification;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private UserAccount userAccount;
    private String appUrl;

    public OnRegistrationCompleteEvent(UserAccount userAccount, String appUrl) {
        super(userAccount);
        this.userAccount = userAccount;
        this.appUrl = appUrl;
    }
}
