package cad.project.service;

import cad.project.playload.UserDTO;
import cad.project.playload.UserResponse;
import jakarta.validation.Valid;

public interface UserService {
    UserDTO addRespo(@Valid UserDTO userDTO);

    UserResponse getAllRespos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO getRespoById(Long userId);

    UserDTO updateRespo(Long userId, @Valid UserDTO userDTO);

    UserDTO deleteRespo(Long userId);
}