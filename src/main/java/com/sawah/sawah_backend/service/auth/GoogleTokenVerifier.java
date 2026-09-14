package com.sawah.sawah_backend.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifier(
            @Value("${google.oauth.client-id}") String clientId,
            @Value("${google.oauth.mobile-client-id}") String mobileClientId
    ) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(List.of(clientId, mobileClientId))
                .build();
    }

    public GoogleUserInfo verify(String idToken) {
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new BadRequestException("google.token.invalid");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            if (!Boolean.TRUE.equals(payload.getEmailVerified()) || payload.getEmail() == null) {
                throw new BadRequestException("google.token.invalid");
            }

            return new GoogleUserInfo(
                    payload.getEmail(),
                    payload.getSubject(),
                    resolveFirstName(payload),
                    resolveLastName(payload)
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new BadRequestException("google.token.invalid");
        }
    }

    private String resolveFirstName(GoogleIdToken.Payload payload) {
        String givenName = (String) payload.get("given_name");
        if (givenName != null && !givenName.isBlank()) {
            return givenName;
        }

        String name = (String) payload.get("name");
        if (name == null || name.isBlank()) {
            return "Google";
        }

        return name.trim().split("\\s+", 2)[0];
    }

    private String resolveLastName(GoogleIdToken.Payload payload) {
        String familyName = (String) payload.get("family_name");
        if (familyName != null && !familyName.isBlank()) {
            return familyName;
        }

        String name = (String) payload.get("name");
        if (name == null || name.isBlank()) {
            return "User";
        }

        String[] nameParts = name.trim().split("\\s+", 2);
        return nameParts.length > 1 ? nameParts[1] : "User";
    }
}


