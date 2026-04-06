package com.finance.controller.user;

import com.finance.dto.UserDto;
import com.finance.dto.UserLoginRequest;
import com.finance.dto.UserRequestDto;
import com.finance.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/user")
//public class UserController {
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register( @Valid @RequestBody UserRequestDto dto){
//        return ResponseEntity.ok(userService.register(dto));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto) {
//        return ResponseEntity.ok(userService.login(dto));
//    }
//
//    @PutMapping("/update")
//    @PreAuthorize("hasRole('ADMIN')")
////    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto){
//        return ResponseEntity.ok(userService.update(dto));
//    }
//}


@Tag(name = "User APIs", description = "User management operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructor-based dependency injection.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     *
     * Access:
     * - Typically invoked by Admin (based on system design)
     *
     * Validations:
     * - Email must be unique
     * - Password must satisfy security constraints (handled via DTO validation)
     *
     * @param dto User registration request
     * @return Created user details (without password)
     */
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRequestDto dto){
        return ResponseEntity.ok(userService.register(dto));
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * Flow:
     * - Credentials are validated using AuthenticationManager
     * - JWT token is generated upon successful authentication
     *
     * @param dto Login request containing email and password
     * @return JWT token
     */
    @Operation(summary = "Login user and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    /**
     * Updates user details.
     *
     * Access:
     * - Restricted to ADMIN only
     *
     * Allowed Updates:
     * - Name
     * - Role
     * - Active status
     *
     * @param dto User data to update
     * @return Updated user details
     */
    @Operation(summary = "Update User (Only Admin)")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto){
        return ResponseEntity.ok(userService.update(dto));
    }
}