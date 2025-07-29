package com.pablo.digitalstore.digital_store_api.auth;

import com.pablo.digitalstore.digital_store_api.exception.UserDisabledException;
import com.pablo.digitalstore.digital_store_api.jwt.JwtService;
import com.pablo.digitalstore.digital_store_api.model.dto.request.AuthRefreshRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.AuthRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.AuthResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.RoleEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.UserEntity;
import com.pablo.digitalstore.digital_store_api.model.enums.Roles;
import com.pablo.digitalstore.digital_store_api.model.mapper.CredentialsMapper;
import com.pablo.digitalstore.digital_store_api.model.mapper.UserMapper;
import com.pablo.digitalstore.digital_store_api.repository.CredentialsRepository;
import com.pablo.digitalstore.digital_store_api.repository.RoleRepository;
import com.pablo.digitalstore.digital_store_api.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AuthServiceImpl implements AuthService {

    private final CredentialsRepository credentialsRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CredentialsMapper credentialsMapper;

    public AuthServiceImpl(CredentialsRepository credentialsRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           CredentialsMapper credentialsMapper) {
        this.credentialsRepository = credentialsRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.credentialsMapper = credentialsMapper;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        CredentialsEntity credentials = credentialsRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!credentials.getUser().getActive()) {
            throw new UserDisabledException("Your account is disabled. Please reactivate it.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String jwt = jwtService.generateToken(credentials);
        String refresh = jwtService.generateRefreshToken(credentials);

        userRepository.save(credentials.getUser());

        credentials.setRefreshToken(refresh);
        credentialsRepository.save(credentials);

        return new AuthResponse(jwt, refresh);
    }

    @Override
    public AuthResponse register(UserRequest request) {
        UserEntity newUser = userMapper.toEntity(request);
        userRepository.save(newUser);

        RoleEntity defaultRole = roleRepository.findByRole(Roles.ROLE_CLIENT)
                .orElseThrow(() -> new RuntimeException("Default roles not found"));

        String hashedPassword = passwordEncoder.encode(request.password());

        CredentialsEntity credentials = credentialsMapper.toEntity(
                request.email(), hashedPassword, newUser, defaultRole
        );
        credentialsRepository.save(credentials);

        String jwt = jwtService.generateToken(credentials);
        String refresh = jwtService.generateRefreshToken(credentials);

        credentials.setRefreshToken(refresh);
        credentialsRepository.save(credentials);

        return new AuthResponse(jwt, refresh);
    }

    @Override
    public AuthResponse refreshToken(AuthRefreshRequest request) {
        String refreshToken = request.refreshToken();

        String email = jwtService.extractUsername(refreshToken);

        CredentialsEntity credentials = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!refreshToken.equals(credentials.getRefreshToken())
                || !jwtService.isTokenValid(refreshToken, credentials)
                || !jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(credentials);
        String newRefreshToken = jwtService.generateRefreshToken(credentials);

        credentials.setRefreshToken(newRefreshToken);
        credentialsRepository.save(credentials);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }


    @Override
    public AuthResponse reactivateUser(AuthRequest request) {
        CredentialsEntity credentials = credentialsRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (credentials.getUser().getActive()) {
            throw new IllegalStateException("Account is already active.");
        }

        if (!passwordEncoder.matches(request.password(), credentials.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        credentials.getUser().setActive(true);
        userRepository.save(credentials.getUser());

        String jwt = jwtService.generateToken(credentials);
        String refresh = jwtService.generateRefreshToken(credentials);

        credentials.setRefreshToken(refresh);
        credentialsRepository.save(credentials);

        return new AuthResponse(jwt, refresh);
    }
}
