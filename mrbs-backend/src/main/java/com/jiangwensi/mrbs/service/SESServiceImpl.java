package com.jiangwensi.mrbs.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.constant.PropKeyConst;
import com.jiangwensi.mrbs.dto.TokenDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.TokenEntity;
import com.jiangwensi.mrbs.enumeration.TokenType;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.repo.TokenRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Service
public class SESServiceImpl implements SESService {

    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    AppProperties appProperties;

    private String from;

    public SESServiceImpl() {

    }

    public SESServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, AppProperties appProperties) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.appProperties = appProperties;
    }

    @Override
    public void sendEmailVerification(UserDto userDto) {

        loadProperties();

        String subject = "[MRBS] Verify Email to Sign Up Meeting Room Booking System";
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Verify Email to Sign Up Meeting Room Booking System</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hi $name,\n" +
                "<br/>\n" +
                "<br/>\n" +
                "Welcome to Meeting Room Booking System.\n" +
                "<br/>\n" +
                "Please click <a href=\"$link\">here</a> to verify your email address and complete the sign up " +
                "process.\n" +
                "\n" +
                "<br/>\n" +
                "<br/>\n" +
                "Regards,\n" +
                "<br/>\n" +
                "Meeting Room Booking System\n" +
                "<br/>\n" +
                "<br/>\n" +
                "\n" +
                "<small>This is an auto generated email. Please do not reply.</small>\n" +
                "</body>\n" +
                "</html>";

        String htmlText = "Hi $name,\n" +
                "\n" +
                "Welcome to Meeting Room Booking System.\n" +
                "Please click here to verify your email address and complete the sign up process.\n" +
                "\n" +
                "Regards,\n" +
                "Meeting Room Booking System\n" +
                "\n" +
                "This is an auto generated email. Please do not reply.";

        TokenDto token = retrieveToken(userDto);

        if(token==null){
            throw new NotFoundException("Verification token is not found for email "+userDto.getEmail());
        }

        String endpoint = "http://localhost:8080/users/verify-email?token=$token";


        sendEmail(userDto, endpoint, subject, htmlBody, htmlText, token);

    }

    public void loadProperties() {
        System.setProperty("aws.accessKeyId", appProperties.getProperty(PropKeyConst.AWS_ACCESS_KEY_ID));
        System.setProperty("aws.secretKey", appProperties.getProperty(PropKeyConst.AWS_SECRET_KEY));
        from = appProperties.getProperty(PropKeyConst.EMAIL_FROM);
    }

    public TokenDto retrieveToken(UserDto userDto) {
//        String token=null;
        List<String> tokens = userDto.getTokens();
        for (String tokenStr : tokens) {
            TokenEntity tokenEntity = tokenRepository.findByToken(tokenStr);
            if (tokenEntity.getType().equals(TokenType.VERIFY_EMAIL.name())) {
               return MyModelMapper.userEntityToUserDtoModelMapper().map(tokenEntity,TokenDto.class);
            }
        }
        return null;
//        return token;
    }

    private void sendEmail(UserDto userDto, String endpoint, String subject, String htmlBody, String htmlText,
                          TokenDto token) {
        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.AP_SOUTHEAST_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(userDto.getEmail()).withCcAddresses(appProperties.getProperty("email.from")))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(htmlBody.replace("$name", userDto.getName()).replace(
                                                    "$link", token.getReturnUrl()+"?token="+token.getToken())))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(htmlText.replace("$name",  userDto.getName()).replace(
                                                    "$link", token.getReturnUrl()+"?token="+token.getToken()))))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource(from);

            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }

    @Override
    public void sendResetForgottenPasswordTokenEmail(UserDto userDto, TokenDto tokenDto) {

        loadProperties();

        String subject = "Reset Forgotten Password for Your Account in Meeting Room Booking System";
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Reset Forgotten Password for Your Account in Meeting Room Booking System</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hi $name,\n" +
                "<br/>\n" +
                "<br/>\n" +
                "We have received your request to reset your forgotten password.\n" +
                "<br/>\n" +
                "Please click <a href=\"$link\">here</a> to reset your password.\n" +
                "\n" +
                "<br/>\n" +
                "<br/>\n" +
                "Regards,\n" +
                "<br/>\n" +
                "Meeting Room Booking System\n" +
                "<br/>\n" +
                "<br/>\n" +
                "\n" +
                "<small>This is an auto generated email. Please do not reply.</small>\n" +
                "</body>\n" +
                "</html>";

        String htmlText = "Hi $name,\n" +
                "\n" +
                "We have received your request to reset your forgotten password.\n" +
                "Please click <a href=\"$link\">here</a> to reset your password.\n" +
                "\n" +
                "Regards,\n" +
                "Meeting Room Booking System\n" +
                "\n" +
                "This is an auto generated email. Please do not reply.";

        String endpoint = "http://localhost:3000/resetForgottenPassword?token=$token";

        sendEmail(userDto, endpoint, subject, htmlBody, htmlText, tokenDto);

    }
}
