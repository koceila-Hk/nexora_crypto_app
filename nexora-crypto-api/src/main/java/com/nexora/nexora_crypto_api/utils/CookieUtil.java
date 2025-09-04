package com.nexora.nexora_crypto_api.utils;

import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static ResponseCookie createAccessTokenCookie(String token, long expirationMs) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(expirationMs)
                .build();
    }

    public static ResponseCookie createRefreshTokenCookie(String token, long expirationMs) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(expirationMs)
                .build();
    }
}
//            Strict	Seulement sur ton domaine	Très sécurisé, mais peut bloquer certaines navigations
//            Lax	Liens normaux OK, POST cross-site bloqué	Bon compromis pour login / refresh token
//            None	Toujours envoyé	Nécessite HTTPS (Secure=true) et utile pour frontend/backend différents