package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.entity.User;
import org.example.userservice.exception.ResourceNotFoundException;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    public UserDTO findById(Long id) {
        return toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    public UserDTO create(UserDTO dto) {
        if (repo.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username already taken: " + dto.getUsername());
        if (repo.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email already used: " + dto.getEmail());
        User user = toEntity(dto);
        user.setId(null);
        return toDto(repo.save(user));
    }

    public UserDTO update(Long id, UserDTO dto) {
        User u = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (dto.getFirstName() != null) u.setFirstName(dto.getFirstName());
        if (dto.getLastName()  != null) u.setLastName(dto.getLastName());
        if (dto.getEmail()     != null) u.setEmail(dto.getEmail());
        return toDto(repo.save(u));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("User", id);
        repo.deleteById(id);
    }

    // ---- mapping helpers ----

    private UserDTO toDto(User u) {
        return UserDTO.builder()
                .id(u.getId()).username(u.getUsername()).email(u.getEmail())
                .firstName(u.getFirstName()).lastName(u.getLastName())
                .membershipDate(u.getMembershipDate()).build();
    }

    private User toEntity(UserDTO d) {
        return User.builder()
                .id(d.getId()).username(d.getUsername()).email(d.getEmail())
                .firstName(d.getFirstName()).lastName(d.getLastName())
                .membershipDate(d.getMembershipDate()).build();
    }
}
