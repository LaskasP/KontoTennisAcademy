package kontopoulos.rest.services.email.impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.UserRepository;
import kontopoulos.rest.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static kontopoulos.rest.models.email.EmailConstant.EMAIL_SUBJECT;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String BREAK_LINE = "\n";
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String sender;

    private MimeMessage createEmail(String username, String email, String url) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setText("Greetings BoredGamer, " + username + BREAK_LINE + "Forgot your password? No problem, just use the link below to reset it" + BREAK_LINE + url + BREAK_LINE + "Happy BoredGaming!");
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        return mimeMessage;
    }

    @Override
    public void sendResetPasswordMail(String email) throws AppUserNotFoundException {
        String username = retrieveUsername(email);
        try {
            javaMailSender.send(createEmail(username, email, "https://maximosstratis.github.io/sofoulakos/"));
        } catch (MessagingException e) {
            log.error("Error while sending mail!!!");
        }
    }

    private String retrieveUsername(String email) throws AppUserNotFoundException {
        AppUserEntity appUserEntity = userRepository.findByEmail(email);
        if (appUserEntity == null) {
            throw new AppUserNotFoundException("User doesn't exist with email: " + email);
        }
        return appUserEntity.getUsername();
    }
}
