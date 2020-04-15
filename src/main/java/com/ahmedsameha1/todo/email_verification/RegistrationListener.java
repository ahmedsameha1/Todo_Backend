package com.ahmedsameha1.todo.email_verification;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static com.ahmedsameha1.todo.security.Constants.EMAIL_VERIFICATION_URL;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        UserAccount userAccount = event.getUserAccount();
        EmailVerificationToken emailVerificationToken = userAccountService.createEmailVerificationToken(userAccount);
        var userAccountEmail = userAccount.getEmail();
        var emailSubject = "Registration Confirmation";
        var emailVerificationUrl = event.getAppUrl()
                + EMAIL_VERIFICATION_URL + "?token=" + emailVerificationToken.getToken();
        var email = new SimpleMailMessage();
        email.setTo(userAccountEmail);
        email.setSubject(emailSubject);
        var emailVerificationMessage = messageSource.getMessage("emailVerificationMessage",
                null, event.getLocale());
        email.setText(emailVerificationMessage + "\r\n" + emailVerificationUrl);
        javaMailSender.send(email);
    }
}
