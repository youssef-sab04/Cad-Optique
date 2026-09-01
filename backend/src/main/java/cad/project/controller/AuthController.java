package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.model.AppRole;
import cad.project.model.Role;
import cad.project.model.User;
import cad.project.playload.CategoryDTO;
import cad.project.playload.UserDTO;
import cad.project.playload.UserResponse;
import cad.project.repositries.RoleRepository;
import cad.project.repositries.UserRepositry;
import cad.project.security.jwt.JwtUtils;
import cad.project.security.request.LoginRequest;
import cad.project.security.request.SignupRequest;
import cad.project.security.response.MessageResponse;
import cad.project.security.response.UserInfoResponse;
import cad.project.security.services.UserDetailsImpl;
import cad.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepositry userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles, userDetails.getEmail() , jwtCookie.toString());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role responsableRole = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(responsableRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role responsableRole = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(responsableRole);
                }
            });
        }
        user.setUserRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null)
            return authentication.getName();
        else
            return "";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ajouter un responsable")
    @PostMapping("/admin/add_responsable")
    public ResponseEntity<UserDTO> addResponsable(@Valid @RequestBody UserDTO userDTO){
        UserDTO userDTOSaved = userService.addRespo(userDTO);
        return new ResponseEntity<>(userDTOSaved, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get responsables")
    @GetMapping("/admin/responsables")
    public ResponseEntity<UserResponse> getAllRespos(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        return new ResponseEntity<>(userService.getAllRespos(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get responsable par id")
    @GetMapping("/admin/responsables/{userId}")
    public ResponseEntity<UserDTO> getRespoById(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getRespoById(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un responsable")
    @PutMapping("/admin/responsables/{userId}")
    public ResponseEntity<UserDTO> updateRespo(@Valid @RequestBody UserDTO userDTO,
                                               @PathVariable Long userId){
        return new ResponseEntity<>(userService.updateRespo(userId, userDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un responsable")
    @DeleteMapping("/admin/responsables/{userId}")
    public ResponseEntity<UserDTO> deleteRespo(@PathVariable Long userId){
        return new ResponseEntity<>(userService.deleteRespo(userId), HttpStatus.OK);
    }




}