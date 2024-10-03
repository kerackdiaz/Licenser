package com._mas1r.licenser.filters;

import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.security.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MasterRepository masterRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            email = jwtUtilService.extractUsername(token);
        } else if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String[] parts = authorizationHeader.substring(4).split("\\.");
            if (parts.length == 2) {
                String publicKeyEncoded = parts[0];
                String signatureEncoded = parts[1];

                MasterAdmin masterAdmin = masterRepository.findByPublicKey(publicKeyEncoded);
                if (masterAdmin != null) {
                    try {
                        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyEncoded)));
                        Signature signature = Signature.getInstance("SHA256withRSA");
                        signature.initVerify(publicKey);
                        signature.update(publicKeyEncoded.getBytes());

                        if (signature.verify(Base64.getDecoder().decode(signatureEncoded))) {
                            UserDetails userDetails = new org.springframework.security.core.userdetails.User(masterAdmin.getUsername(), "", new ArrayList<>());
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            if (jwtUtilService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}