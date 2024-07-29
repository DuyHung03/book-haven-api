package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.entity.User;
import com.duyhung.bookstoreapi.entity.VerifyCode;
import com.duyhung.bookstoreapi.repository.UserRepository;
import com.duyhung.bookstoreapi.repository.VerifyCodeRepository;
import com.duyhung.bookstoreapi.util.CodeGenerator;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.annotation.PostConstruct;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static jakarta.mail.Message.RecipientType.TO;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final UserRepository userRepository;
    private final VerifyCodeRepository verifyCodeRepository;
    @Value("${mail.client-secret-credential}")
    private String credentialPath;
    @Value("${mail.application-name}")
    private String applicationName;
    private Gmail service;

    @PostConstruct
    private void init() {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            this.service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                    .setApplicationName(applicationName)
                    .build();
            log.info("Gmail service initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize Gmail service: ", e);
            throw new RuntimeException("Failed to initialize Gmail service", e);
        }
    }

    private Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                jsonFactory, new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(credentialPath))
                ));
        log.info("Loaded client secrets from {}", credentialPath);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        log.info("Google Authorization Code Flow initialized.");

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        log.info("Local Server Receiver initialized on port 8888.");
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private void sendMail(String toEmail, String subject, String message) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("duyhung1892003@gmail.com"));
        email.addRecipient(TO, new InternetAddress(toEmail));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = service.users().messages().send("me", msg).execute();
            log.info("Message sent successfully. Message id: {}", msg.getId());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                log.error("Unable to send message: {}", e.getDetails());
            } else {
                throw e;
            }
        }
    }

    public String sendEmailVerifyCode(String email) throws Exception {
        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "User not found";
        }
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        VerifyCode code = new VerifyCode();
        code.setCode(CodeGenerator.generate6DigitCode());
        code.setExpirationTime(expirationTime);
        verifyCodeRepository.save(code);

        String text = "Your verification code is " + code.getCode() + ". It will expire in 5 minutes.";

        // Send the email with the token
        sendMail(user.getEmail(), "Book Haven Verify Code", text);

        return "Verify code sent";
    }
}
