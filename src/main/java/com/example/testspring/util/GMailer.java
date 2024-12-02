package com.example.testspring.util;

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
import org.apache.commons.codec.binary.Base64;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

public class GMailer {

    private static final String TEST_EMAIL = "promocytech@gmail.com";  // Ton adresse d'envoi
    private static final String CLIENT_SECRET_FILE = "client_secret_1049703125334-n5b8tgccv8egh5vmrujjvec2qj7ashmm.apps.googleusercontent.com(2emeversion).json";
    private final Gmail service;

    public GMailer() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Gmailer2")
                .build();
    }

    // Charger les credentials OAuth2 pour l'application
    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        InputStream in = GMailer.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE);
        if (in == null) {
            throw new IOException("Fichier JSON introuvable dans les ressources : " + CLIENT_SECRET_FILE);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // Méthode d'envoi d'email
    public void sendMail(String subject, String messageText, String recipientEmail) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(TO, new InternetAddress(recipientEmail));  // L'adresse du destinataire

        email.setSubject(subject);
        email.setText(messageText);  // Le corps du message

        // Convertir l'email en format MIME pour l'API Gmail
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

        Message msg = new Message();
        msg.setRaw(encodedEmail);  // Message prêt à l'envoi

        try {
            msg = service.users().messages().send("me", msg).execute();  // Envoi via Gmail API
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }

    // Pour tester l'envoi de l'email
    public static void main(String[] args) throws Exception {
        // Exemple d'envoi
        new GMailer().sendMail("Confirmation d'inscription",
                "Bonjour, vous êtes inscrit avec succès à CYTECH.\n" +
                        "Nous vous souhaitons une excellente année.",
                "promocytech@gmail.com");  // L'adresse email du destinataire
    }
}
