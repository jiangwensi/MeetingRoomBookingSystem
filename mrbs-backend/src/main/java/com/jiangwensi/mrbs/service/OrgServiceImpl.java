package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.OrganizationDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.OrganizationEntity;
import com.jiangwensi.mrbs.entity.RoomEntity;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.model.request.org.OrganizationRequest;
import com.jiangwensi.mrbs.repo.BookingRepository;
import com.jiangwensi.mrbs.repo.OrgRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
@Service
@Slf4j
public class OrgServiceImpl implements OrgService {
    private final OrgRepository organizationRepository;
    private final BookingRepository bookingRepos;
    private final UserRepository userRepository;
    @Autowired
    private RoomService roomService;

    public OrgServiceImpl(OrgRepository organizationRepository, BookingRepository bookingRepos, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.bookingRepos = bookingRepos;
        this.userRepository = userRepository;
    }

    @Override
    public List<OrganizationDto> search(String name, Boolean active) {
        List<OrganizationEntity> organizationEntities = organizationRepository.findByNameAndActive(name,active);

        if (organizationEntities == null) {
            throw new NotFoundException("Unable to find organization name:" + name + ",active:" + active);
        }
        List<OrganizationDto> returnValue = new ArrayList<OrganizationDto>();

        organizationEntities.forEach(o -> {
            OrganizationDto organizationDto = new OrganizationDto();
            MyModelMapper.organizationEntityToOrganizationDtoModelMapper().map(o, organizationDto);
            returnValue.add(organizationDto);
        });

        return returnValue;

    }

    @Override
    public OrganizationDto viewOrganization(String publicId) {


        OrganizationEntity organizationEntity = organizationRepository.findByPublicId(publicId);
        if (organizationEntity == null) {
            throw new NotFoundException("Unable to find organization id:" + publicId);
        }

        OrganizationDto returnValue = new OrganizationDto();
        MyModelMapper.organizationEntityToOrganizationDtoModelMapper().map(organizationEntity, returnValue);
        return returnValue;

    }

    @Override
    @Transactional
    public OrganizationDto create(OrganizationRequest request) {

        String name = request.getName();
        String description = request.getDescription();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();

        OrganizationEntity organizationEntity = new OrganizationEntity(UUID.randomUUID().toString());
        organizationEntity.setActive(active);
        organizationEntity.setDescription(description);
        organizationEntity.setName(name);

        if (admins != null && admins.size() > 0) {

            List<UserEntity> userEntities = new ArrayList<>();
            for (String a : admins) {
                log.debug("a=" + a);
                UserEntity userEntity = userRepository.findByEmail(a);
                if (userEntity == null) {
                    throw new NotFoundException("Unable to find user by email:" + a);
                }
                userEntity.getIsAdminOfOrganizations().add(organizationEntity);
                userEntities.add(userEntity);
            }
            organizationEntity.setAdmins(userEntities);
        }

        organizationEntity = organizationRepository.save(organizationEntity);
        OrganizationDto returnValue = new OrganizationDto();
        MyModelMapper.organizationEntityToOrganizationDtoModelMapper().map(organizationEntity, returnValue);

        return returnValue;
    }

    @Override
    public OrganizationDto update(OrganizationRequest request) {

        String publicId = request.getPublicId();
        String name = request.getName();
        String description = request.getDescription();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();

        OrganizationEntity organizationEntity = organizationRepository.findByPublicId(publicId);
        if (organizationEntity == null) {
            throw new NotFoundException("Unable to find organization by id:" + publicId);
        }

        if (!MyStringUtils.isEmpty(name)) {
            organizationEntity.setName(name);
        }
        if (!MyStringUtils.isEmpty(description)) {
            organizationEntity.setDescription(description);
        }
        if (active != null) {
            organizationEntity.setActive(active);
        }
        if (admins != null && admins.size() > 0) {

            List<UserEntity> userEntities = new ArrayList<>();

            admins.forEach(a -> {
                UserEntity userEntity = userRepository.findByPublicId(a);
                if (userEntities == null) {
                    throw new NotFoundException("Unable to find user by id:" + a);
                }
                userEntities.add(userEntity);
            });

            organizationEntity.setAdmins(userEntities);

        }

        organizationEntity = organizationRepository.save(organizationEntity);

        OrganizationDto returnValue = new OrganizationDto();
        MyModelMapper.organizationEntityToOrganizationDtoModelMapper().map(organizationEntity, returnValue);

        return returnValue;
    }

    @Override
    @Transactional
    public void delete(String publicId) {
        OrganizationEntity organizationEntity = organizationRepository.findByPublicId(publicId);
        if (organizationEntity == null) {
            throw new NotFoundException("Unable to find organization by id:" + publicId);
        }
        organizationEntity
                .getRooms()
                .stream()
                .map(r->r.getId())
                .forEach(r->bookingRepos.deleteByRoomId(r));
        organizationEntity.getRooms().stream().forEach(r->roomService.deleteRoom(r.getPublicId()));
        organizationRepository.deleteByPublicId(publicId);
    }


    @Override
    public List<UserDto> listAdminByOrg(String orgPublicId) {

//        if(!userService.hasAuthorizedRoleOrAccessingMyOrganization(RoleName.SYSADM.getName(),orgPublicId)){
//            throw new AccessDeniedException("You are not allowed to view organization "+orgPublicId);
//        };
        List<UserDto> returnValue = new ArrayList<>();
        List<UserEntity> userEntities = organizationRepository.findByPublicId(orgPublicId).getAdmins();
        if (userEntities != null && userEntities.size() > 0) {
            for (UserEntity userEntity : userEntities) {
                UserDto userDto = new UserDto();
                MyModelMapper.userEntityToUserDtoModelMapper().map(userEntity, userDto);
                returnValue.add(userDto);
            }
        }
        return returnValue;
    }

    @Override
    public List<RoomDto> listRoomsByOrg(String orgPublicId) {

//        if(!userService.hasAuthorizedRoleOrAccessingMyOrganization(RoleName.SYSADM.getName(),orgPublicId)){
//            throw new AccessDeniedException("You are not allowed to view organization "+orgPublicId);
//        };
        List<RoomDto> returnValue = new ArrayList<>();
        List<RoomEntity> roomEntities = organizationRepository.findByPublicId(orgPublicId).getRooms();
        if (roomEntities != null && roomEntities.size() > 0) {
            for (RoomEntity roomEntity : roomEntities) {
                RoomDto roomDto = new RoomDto();
                MyModelMapper.roomEntityToDtoModelMapper().map(roomEntity, roomDto);
                returnValue.add(roomDto);
            }
        }
        return returnValue;
    }
}
