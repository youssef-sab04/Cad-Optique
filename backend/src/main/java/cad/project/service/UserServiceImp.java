package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.AppRole;
import cad.project.model.Role;
import cad.project.model.User;
import cad.project.playload.UserDTO;
import cad.project.repositries.RoleRepository;
import cad.project.repositries.UserRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import cad.project.playload.UserResponse;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements  UserService {

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDTO addRespo(UserDTO userDTO) {

        Role responsableRole = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(AppRole.ROLE_RESPONSABLE);
                    return roleRepository.save(newRole);
                });

        Set<Role> responsableRoles = Set.of(responsableRole);


        User user = modelMapper.map(userDTO , User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRoles(responsableRoles);

        userRepositry.save(user);
        return modelMapper.map(user , UserDTO.class);
    }

    @Override
    public UserResponse getAllRespos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<User> userPage = userRepositry.findByUserRoles_RoleName(AppRole.ROLE_RESPONSABLE, pageDetails);

        List<User> users = userPage.getContent();
        if (users.isEmpty())
            throw new APIException("No Responsable Created till now");

        List<UserDTO> userDTOS = users.stream().map(this::toDTO).toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOS);
        userResponse.setPageNumber(userPage.getNumber());
        userResponse.setPageSize(userPage.getSize());
        userResponse.setTotalElements(userPage.getTotalElements());
        userResponse.setTotalPages(userPage.getTotalPages());
        userResponse.setLastPage(userPage.isLast());
        return userResponse;
    }

    @Override
    public UserDTO getRespoById(Long userId) {
        User user = userRepositry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return toDTO(user);
    }

    @Override
    public UserDTO updateRespo(Long userId, UserDTO userDTO) {
        User userFromDb = userRepositry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        userFromDb.setNom(userDTO.getNom());
        userFromDb.setPrenom(userDTO.getPrenom());
        userFromDb.setUserName(userDTO.getUserName());
        userFromDb.setEmail(userDTO.getEmail());
        userFromDb.setCin(userDTO.getCin());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank())
            userFromDb.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepositry.save(userFromDb);
        return toDTO(userFromDb);
    }

    @Override
    public UserDTO deleteRespo(Long userId) {
        User user = userRepositry.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        userRepositry.delete(user);
        return toDTO(user);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.setPassword(null);
        dto.setRoles(user.getUserRoles() == null ? Set.of()
                : user.getUserRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toSet()));
        return dto;
    }


}
