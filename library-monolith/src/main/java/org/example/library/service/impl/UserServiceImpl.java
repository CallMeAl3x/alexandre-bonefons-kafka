package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.UserDTO;
import org.example.library.exception.ResourceNotFoundException;
import org.example.library.model.User;
import org.example.library.repository.UserRepository;
import org.example.library.service.UserService;
import org.example.library.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final DTOMapper mapper;

    @Override public List<UserDTO> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override public UserDTO findById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    @Override public UserDTO create(UserDTO dto) {
        if (repo.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username already taken: " + dto.getUsername());
        if (repo.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email already used: " + dto.getEmail());
        User user = mapper.toEntity(dto);
        user.setId(null);
        return mapper.toDto(repo.save(user));
    }

    @Override public UserDTO update(Long id, UserDTO dto) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName()  != null) existing.setLastName(dto.getLastName());
        if (dto.getEmail()     != null) existing.setEmail(dto.getEmail());
        return mapper.toDto(repo.save(existing));
    }

    @Override public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("User", id);
        repo.deleteById(id);
    }
}
