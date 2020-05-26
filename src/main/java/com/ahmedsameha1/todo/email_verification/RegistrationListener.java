package com.ahmedsameha1.todo.email_verification;

import com.ahmedsameha1.todo.domain_model.EmailVerificationToken;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ahmedsameha1.todo.Constants.EMAIL_VERIFICATION_URL;


@Component
public class RegistrationListener implements ApplicationListener<NeedEmailVerificationToken> {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSource messageSource;

    @Async
    @Override
    public void onApplicationEvent(NeedEmailVerificationToken event) {
        UserAccount userAccount = event.getUserAccount();
        EmailVerificationToken emailVerificationToken = userAccountService.createEmailVerificationToken(userAccount);
        var emailVerificationUrl = event.getAppUrl()
                + EMAIL_VERIFICATION_URL + "?token=" + emailVerificationToken.getToken();
        var emailVerificationMessage = messageSource.getMessage("emailVerificationMessage",
                null, event.getLocale());
        sendEmail(userAccount, emailVerificationMessage, emailVerificationUrl);
    }

    private void sendEmail(UserAccount userAccount, String emailVerificationMessage, String emailVerificationUrl) {
        var email = new SimpleMailMessage();
        email.setTo(userAccount.getEmail());
        email.setSubject("Registration Confirmation");
        email.setText(emailVerificationMessage + "\r\n" + emailVerificationUrl);
        javaMailSender.send(email);
    }
}
