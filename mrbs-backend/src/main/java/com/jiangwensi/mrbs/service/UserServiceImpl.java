package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.*;
import com.jiangwensi.mrbs.enumeration.RoleName;
import com.jiangwensi.mrbs.enumeration.TokenType;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.model.request.user.UpdateUserRequest;
import com.jiangwensi.mrbs.repo.*;
import com.jiangwensi.mrbs.security.UserPrincipal;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import com.jiangwensi.mrbs.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private RoleRepository roleRepository;
    private OrgRepository orgRepository;
    private RoomRepository roomRepo;
    private BCryptPasswordEncoder encoder;
    private BookingRepository bookingRepository;

    @Autowired
    private RoleService roleService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository, OrgRepository orgRepository, RoomRepository roomRepo, BCryptPasswordEncoder encoder, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.orgRepository = orgRepository;
        this.roomRepo = roomRepo;
        this.encoder = encoder;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public UserDto findUserByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return null;
        }
        UserDto returnValue = new UserDto();
        MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity, returnValue);
        return returnValue;
    }

    @Override
    @Transactional
    public UserDto createUser(String name, String email, String password, String returnUrl) {
        UserEntity userEntity = new UserEntity(UUID.randomUUID().toString());
        RoleEntity roleEntity = roleRepository.findByName(RoleName.USER.toString());
        String passwordEnc = encoder.encode(password);
        Long tokenValidity = Long.valueOf(AppProperties.getAppProperties().getProperty("email.verify.token.validity"));
        String emailVerificationToken = TokenUtils.generateToken(email, tokenValidity);

        userEntity.setEmailVerified(false);
        userEntity.setEmail(email);
        userEntity.setName(name);
        userEntity.setPassword(passwordEnc);

        userEntity.setRoles(Arrays.asList(roleEntity));

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(emailVerificationToken);
        tokenEntity.setUser(userEntity);
        tokenEntity.setType(TokenType.VERIFY_EMAIL.name());
        tokenEntity.setReturnUrl(returnUrl);

        userEntity.getTokens().add(tokenEntity);
        userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity,returnValue);
        roleEntity.getUsers().add(userEntity);
        roleRepository.save(roleEntity);

        return returnValue;
    }

    @Override
    @Transactional
    public UserDto regenerateEmailVerifyToken(String name, String email, String password, String returnUrl) {
        UserEntity userEntity = userRepository.findByEmail(email);
        String passwordEnc = encoder.encode(password);
        Long tokenValidity = Long.valueOf(AppProperties.getAppProperties().getProperty("email.verify.token.validity"));
        String emailVerificationToken = TokenUtils.generateToken(email, tokenValidity);

        userEntity.setName(name);
        userEntity.setPassword(passwordEnc);

        List<TokenEntity> tokenEntities = userEntity.getTokens();
        userService.removeObsoleteToken(tokenEntities);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(emailVerificationToken);
        tokenEntity.setUser(userEntity);
        tokenEntity.setType(TokenType.VERIFY_EMAIL.name());
        tokenEntity.setReturnUrl(returnUrl);
        tokenEntities.add(tokenEntity);

        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        MyModelMapper.userEntityToUserDtoModelMapper().map(savedUserEntity, returnValue);
        return returnValue;
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void removeObsoleteToken(List<TokenEntity> tokenEntities) {
        for (TokenEntity tokenEntity : tokenEntities) {
            if (tokenEntity.getType().equals(TokenType.VERIFY_EMAIL.name())) {
                tokenRepository.delete(tokenEntity);
                tokenEntities.remove(tokenEntity);
                break;
            }
        }
    }

    @Override
    public UserDto updatePassword(String email, String password) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setPassword(encoder.encode(password));
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        new ModelMapper().map(updatedUserEntity, returnValue);
        return returnValue;
    }

    @Override
    public List<UserDto> search(String name, String email, List<String> role, List<Boolean> active,
                                List<Boolean> verified,Boolean verbose) {
        List<UserEntity> userEntities = new ArrayList<>();
        boolean nameEmpty = MyStringUtils.isEmpty(name);
        boolean emailEmpty = MyStringUtils.isEmpty(email);

        if (role == null || role.size() == 0) {
            role = new ArrayList<>();
            List<String> roleOptions = roleService.listAllRoles();
            roleOptions.forEach(role::add);
        }
        if (active == null || active.size() == 0) {
            active = new ArrayList<>();
            active.add(true);
            active.add(false);
        }
        if (verified == null || verified.size() == 0) {
            verified = new ArrayList<>();
            verified.add(true);
            verified.add(false);
        }

        if (!nameEmpty && !emailEmpty) {

            userEntities = userRepository.searchByAllFields(email, name, role, active, verified);

        } else if (!nameEmpty && emailEmpty) {

            userEntities = userRepository.searchByName(name, role, active, verified);

        } else if (nameEmpty && !emailEmpty) {

            userEntities = userRepository.searchByEmail(email, role, active, verified);

        } else if (nameEmpty && emailEmpty) {

            userEntities = userRepository.search(role, active, verified);


        }
        List<UserDto> returnValue = new ArrayList<>();


        if (userEntities != null && userEntities.size() > 0) {
            for (UserEntity e : userEntities) {
                UserDto userDto = new UserDto();
                if(verbose==null || verbose==false) {
                    e.setBookings(null);
                    e.setIsAdminOfOrganizations(null);
                    e.setIsAdminOfRooms(null);
                    e.setIsUserOfRooms(null);
                }

                MyModelMapper.userEntityToUserDtoModelMapper().map(e, userDto);
                returnValue.add(userDto);
            }
        }

        return returnValue;
    }

    @Override
    public UserDto findUserByPublicId(String publicId) {
        UserEntity userEntity = userRepository.findByPublicId(publicId);
        UserDto returnValue = new UserDto();
        MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity, returnValue);
        return returnValue;
    }

    @Override
    @Transactional
    public void deleteUser(String publicId) {
        log.info("deleteUser publicId:" + publicId);
        //delete user's booking
        UserEntity userEntity = userRepository.findByPublicId(publicId);
        if (userEntity == null) {
            throw new NotFoundException("Unable to find user");
        }
        bookingRepository.deleteBookingByBookedBy(userEntity.getId());

        List<RoleEntity> roleEntities = userEntity.getRoles();
        for (RoleEntity roleEntity : roleEntities) {
            roleEntity.getUsers().remove(userEntity);
            roleRepository.save(roleEntity);
        }

        userRepository.delete(userEntity);
    }


    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request) {
        UserEntity userEntity = userRepository.findByPublicId(request.getPublicId());
        if (userEntity == null) {
            throw new NotFoundException("Unable to find user by id:" + request.getPublicId());
        }
        userEntity.setName(request.getName());
        userEntity.setActive(request.getActive());

        List<RoleEntity> oldRoleEntities = userEntity.getRoles();
        List<RoleEntity> newRoleEntities = new ArrayList<>();
        for (String str :  request.getRoles()) {
            RoleEntity re = roleRepository.findByName(str);
            if (re == null) {
                throw new InvalidInputException("Wrong role:" + str);
            }
            newRoleEntities.add(re);
        }

        oldRoleEntities.forEach(oldRoleEntity -> {
            oldRoleEntity.getUsers().remove(userEntity);
            roleRepository.save(oldRoleEntity);
        });

        newRoleEntities.forEach(newRoleEntity -> {
            newRoleEntity.getUsers().add(userEntity);
            roleRepository.save(newRoleEntity);
        });

        userEntity.setRoles(newRoleEntities);
        userRepository.save(userEntity);
    }

    @Override
    public UserDto updateMyProfile(String publicId, String name, String email, String changeEmailReturnUrl) {
        UserEntity userEntity = userRepository.findByPublicId(publicId);
        boolean emailUpdated = false;
        if (!MyStringUtils.isEmpty(email) && !email.toUpperCase().equals(userEntity.getEmail().toUpperCase())) {
            if (MyStringUtils.isEmpty(changeEmailReturnUrl)) {
                throw new InvalidInputException("changeEmailReturnUrl is not set to validate updated email");
            }

            tokenService.deleteObsoleteTokenVerifyEmail(publicId);
            tokenService.generateVerifyUpdatedEmailToken(email, changeEmailReturnUrl, publicId);

        }

        if (!MyStringUtils.isEmpty(name)) {
            userEntity.setName(name);
            userEntity = userRepository.save(userEntity);
        }

        UserDto returnValue = new UserDto();
        MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity, returnValue);

        return returnValue;

    }

    @Override
    public UserDto emailVerified(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setEmailVerified(true);
        userRepository.save(userEntity);
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("loadUserByUsername is called. email: " + email);
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return null;
        }
        UserPrincipal returnValue = new UserPrincipal();
        returnValue.setEnabled(userEntity.isActive() && userEntity.isEmailVerified());
        returnValue.setPassword(userEntity.getPassword());
        returnValue.setUsername(userEntity.getEmail());
        returnValue.setName(userEntity.getName());
        returnValue.setPublicId(userEntity.getPublicId());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<RoleEntity> roleEntities = userEntity.getRoles();
        for (RoleEntity re : roleEntities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(re.getName()));
        }
        returnValue.setAuthorities(grantedAuthorities);
        return returnValue;
    }



    @Override
    public boolean hasAuthorizedRoleOrAccessingMyOrganization(String authorizedRole, String orgPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) auth.getAuthorities();
        for(GrantedAuthority ga: grantedAuthorities){
            if(ga.getAuthority().equals(authorizedRole)){
                return true;
            }
        }

        List<String> admins = orgRepository.findByPublicId(orgPublicId).getAdmins().stream().map(e->e.getPublicId()).collect(Collectors.toList());
        UserDto userDto = findUserByEmail(auth.getName());
        for(String admin: admins){
            if(userDto.getPublicId().equals(admin)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAccessedByRoomUser(String roomId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = findUserByEmail(auth.getName());

        List<String> roomUsers = roomRepo.findByPublicId(roomId).getUsers().stream().map(e->e.getPublicId()).collect(Collectors.toList());
        if (roomUsers != null && roomUsers.size() > 0) {
            for (String roomUser : roomUsers) {
                if (roomUser.equals(userDto.getPublicId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAccessedByTargetRole(String role) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = findUserByEmail(auth.getName());
        List<String> roles = userDto.getRoles();
        if (roles != null && roles.size() > 0) {
            for (String e : roles) {
                if (e.equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAccessedByRoomAdmin(String bookingPublicId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = findUserByEmail(auth.getName());
        BookingEntity bookingEntity = bookingRepository.findByPublicId(bookingPublicId);
        RoomEntity roomEntity = roomRepo.findByPublicId(bookingEntity.getRoom().getPublicId());
        List<String> roomAdms = roomEntity.getAdmins().stream().map(e->e.getPublicId()).collect(Collectors.toList());
        if (roomAdms != null && roomAdms.size() > 0) {
            for (String roomAdm : roomAdms) {
                if (roomAdm.equals(userDto.getPublicId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAccessingMyBooking(String bookingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByEmail(auth.getName());
        BookingEntity bookingEntity = bookingRepository.findByPublicId(bookingId);
        if (userEntity.getId()==bookingEntity.getBookedBy().getId()) {
            return true;
        }
        return false;
    }




    @Override
    public boolean isAccessingMyOrg(String orgPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> isAdminOfOrganizations =
                userRepository.findByEmail(auth.getName()).getIsAdminOfOrganizations().stream().map(e->e.getPublicId()).collect(Collectors.toList());
        if (isAdminOfOrganizations != null) {
            return isAdminOfOrganizations.contains(orgPublicId);
        }
        return false;
    }


    @Override
    public boolean isAccessingMyRoomOrgAdmin(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);
        UserEntity userEntity = userRepository.findByEmail(auth.getName());
        OrganizationEntity orgEntity = orgRepository.findByPublicId(roomEntity.getOrganization().getPublicId());

        List<String> orgAdmins = orgEntity.getAdmins().stream().map(e->e.getPublicId()).collect(Collectors.toList());
        for (String admin : orgAdmins) {
            if (userEntity.getPublicId().equals(admin)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOrgAdminAccessingRoom(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByEmail(auth.getName());

        List<String> roomsInMyOrganizations = new ArrayList<>();
        List<String> isAdminOfOrganizations = userEntity.getIsAdminOfOrganizations().stream().map(e->e.getPublicId()).collect(Collectors.toList());
        if (isAdminOfOrganizations != null) {
            List<OrganizationEntity> organizationEntities =
                    isAdminOfOrganizations.stream().map(e->orgRepository.findByPublicId(e)).collect(Collectors.toList());
            if (organizationEntities != null) {
                organizationEntities.forEach(e -> roomsInMyOrganizations.addAll(e.getRooms().stream().map(r->r.getPublicId()).collect(Collectors.toList())));
            }
        }

        return roomsInMyOrganizations.contains(roomPublicId);
    }

    @Override
    public boolean isRoomAdminAccessingRoom(String roomPublicId) {
        UserDto userDto = getUserDto();
        List<String> isAdminOfRooms = userDto.getIsAdminOfRooms();
        if (isAdminOfRooms != null) {
            return isAdminOfRooms.contains(roomPublicId);
        }
        return false;
    }

    public UserDto getUserDto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findUserByEmail(auth.getName());
    }

    @Override
    public boolean isUserAccessingRoom(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);
        List<UserEntity> userEntitys =
                roomEntity.getUsers().stream().filter(e -> e.getEmail().equalsIgnoreCase(auth.getName())).collect(Collectors.toList());
        if (userEntitys != null && userEntitys.size() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSysadmAccessingRoom(String publicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if(authority.getAuthority().equalsIgnoreCase("SYSADM")){
                return true;
            }
        }
        return false;
    }

}
