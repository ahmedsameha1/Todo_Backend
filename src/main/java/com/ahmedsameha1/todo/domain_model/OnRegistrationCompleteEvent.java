package com.ahmedsameha1.todo.domain_model;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private UserAccount userAccount;
    private String appUrl;

    public OnRegistrationCompleteEvent(UserAccount userAccount, String appUrl) {
        super(userAccount);
        this.userAccount = userAccount;
        this.appUrl = appUrl;
    }
}
