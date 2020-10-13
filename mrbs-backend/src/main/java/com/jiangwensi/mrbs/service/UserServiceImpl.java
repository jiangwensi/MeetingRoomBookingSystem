package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.RoleDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.RoleEntity;
import com.jiangwensi.mrbs.entity.TokenEntity;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.enumeration.RoleName;
import com.jiangwensi.mrbs.enumeration.TokenType;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.model.response.user.UserResponseItem;
import com.jiangwensi.mrbs.repo.RoleRepository;
import com.jiangwensi.mrbs.repo.TokenRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.security.UserPrincipal;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import com.jiangwensi.mrbs.utils.TokenUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SESService SESService;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        tokenEntity.setType(TokenType.VERIFY_EMAIL.toString());
        tokenEntity.setReturnUrl(returnUrl);

        userEntity.getTokens().add(tokenEntity);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto returnValue = new ModelMapper().map(savedUserEntity, UserDto.class);

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
        for (TokenEntity tokenEntity : tokenEntities) {
            if (tokenEntity.getType().equals(TokenType.VERIFY_EMAIL)) {
                tokenRepository.delete(tokenEntity);
                tokenEntities.remove(tokenEntity);
                break;
            }
        }

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(emailVerificationToken);
        tokenEntity.setUser(userEntity);
        tokenEntity.setType(TokenType.VERIFY_EMAIL.toString());
        tokenEntity.setReturnUrl(returnUrl);
        tokenEntities.add(tokenEntity);

        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto returnValue = new ModelMapper().map(savedUserEntity, UserDto.class);

        return returnValue;
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
                                List<Boolean> verified) {
        List<UserEntity> userEntities = new ArrayList<>();
        boolean nameEmpty = MyStringUtils.isEmpty(name);
        boolean emailEmpty = MyStringUtils.isEmpty(email);
        boolean roleEmpty = (role == null || role.size() == 0) ? true : false;

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

            userEntities = userRepository.searchByName(name,role,active,verified);

        } else if (nameEmpty && !emailEmpty) {

            userEntities = userRepository.searchByEmail(email,role,active,verified);

        } else if (nameEmpty && emailEmpty) {

            userEntities = userRepository.search(role,active,verified);

//            userEntities = IteratorUtils.toList(userRepository.findAll().iterator());

        }
//        List<UserEntity> userEntities = userRepository.search(name);
        List<UserDto> returnValue = new ArrayList<>();


        if (userEntities != null && userEntities.size() > 0) {
            for (UserEntity e : userEntities) {
                UserDto userDto = new UserDto();
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
        logger.info("deleteUser publicId:" + publicId);
        UserEntity userEntity = userRepository.findByPublicId(publicId);
        if (userEntity == null) {
            throw new NotFoundException("Unable to find user");
        }

        List<RoleEntity> roleEntities = userEntity.getRoles();
        for (RoleEntity roleEntity : roleEntities) {
            roleEntity.getUsers().remove(userEntity);
            roleRepository.save(roleEntity);
        }

//        List<TokenEntity> tokenEntities = userEntity.getTokens();
////        tokenRepository.deleteAll(tokenEntities);

        userRepository.delete(userEntity);
    }

    //TODO to allow editing more fields in future
    @Override
    public UserDto editProfile(String publicId, String name) {
        logger.info("editProfile publicId:" + publicId + ", name=" + name);
        UserEntity userEntity = userRepository.findByPublicId(publicId);
        if (!MyStringUtils.isEmpty(name)) {
            userEntity.setName(name);
        }
        userEntity=userRepository.save(userEntity);
        UserDto returnValue = new ModelMapper().map(userEntity,UserDto.class);
//        MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity, returnValue);

        return returnValue;
    }

    @Override
    @Transactional
    public void updateUser(String publicId, String name, List<String> roles, Boolean active) {

        UserEntity userEntity = userRepository.findByPublicId(publicId);
        if(userEntity==null){
            throw new NotFoundException("Unable to find user by id:"+publicId);
        }
        userEntity.setName(name);
        userEntity.setActive(active);

        List<RoleEntity> oldRoleEntities = userEntity.getRoles();
        List<RoleEntity> newRoleEntities = new ArrayList<>();
        for(String str: roles){
            RoleEntity re = roleRepository.findByName(str);
            if(re==null){
                throw new InvalidInputException("Wrong role:"+str);
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
        if(!MyStringUtils.isEmpty(email) && !email.toUpperCase().equals(userEntity.getEmail().toUpperCase())){
            if (MyStringUtils.isEmpty(changeEmailReturnUrl)) {
                throw new InvalidInputException("changeEmailReturnUrl is not set to validate updated email");
            }

            //remove existing verify email token
//            List<TokenEntity> tokenEntities =
//                    userEntity.getTokens().stream().filter(e->!e.getType().equals(TokenType.VERIFY_EMAIL)).collect(Collectors.toList());
//            TokenEntity existingToken = tokenRepository.findByUserIdAndType(userEntity.getId(),
//                    TokenType.VERIFY_EMAIL.name());
//            if(existingToken!=null) {
//                tokenRepository.delete(existingToken);
//                userEntity.getTokens().remove(existingToken);
//                userRepository.save(userEntity);
//            }

            tokenService.deleteObsoleteTokenVerifyEmail(publicId);
            tokenService.generateVerifyUpdatedEmailToken(email, changeEmailReturnUrl, publicId);

        }

        if(!MyStringUtils.isEmpty(name)){
            userEntity.setName(name);
            userEntity = userRepository.save(userEntity);
        }

        UserDto returnValue = new UserDto();
                MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity, returnValue);

        return returnValue;

    }


    @Override
    @Transactional
    public void changeUserStatus(String publicId, boolean active) {
        UserEntity userEntity = userRepository.findByPublicId(publicId);
        userEntity.setActive(active);
        userRepository.save(userEntity);
//        userRepository.changeUserStatus(publicId,active);
//        UserDto returnValue = new UserDto();
//        return;
    }

    @Override
    @Transactional
    public List<RoleDto> changeUserRoles(String publicId, List<String> changeRoles) {
        UserEntity userEntity = userRepository.findByPublicId(publicId);

        List<RoleEntity> oldRoleEntities = userEntity.getRoles();
        List<RoleEntity> newRoleEntities = new ArrayList<>();

        for (String str : changeRoles) {
            newRoleEntities.add(roleRepository.findByName(str));
        }

        oldRoleEntities.forEach(oldRoleEntity -> {
            oldRoleEntity.getUsers().remove(userEntity);
            roleRepository.save(oldRoleEntity);
        });

        newRoleEntities.forEach(newRoleEntity -> {
            newRoleEntity.getUsers().add(userEntity);
            roleRepository.save(newRoleEntity);
        });

        List<RoleDto> returnValue = new ArrayList<>();
        returnValue = new ModelMapper().map(newRoleEntities, new TypeToken<List<RoleDto>>() {
        }.getType());

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
        logger.debug("loadUserByUsername is called. email: " + email);
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return null;
        }
        UserPrincipal returnValue = new UserPrincipal();
        returnValue.setEnabled(userEntity.isActive()&&userEntity.isEmailVerified());
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


}
