package com.proj.test.salarymanagement.mapper;

import com.proj.test.salarymanagement.dto.UserRecordDto;
import com.proj.test.salarymanagement.dto.UserResponseDto;
import com.proj.test.salarymanagement.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<UserEntity> mapToEntities(List<UserRecordDto> recordDtoList){
        final List<UserEntity> userEntities = recordDtoList.stream().map(recordDto -> mapToEntity(recordDto))
                .collect(Collectors.toList());
        return userEntities;
    }

    public UserEntity mapToEntity(UserRecordDto recordDto){
        return UserEntity.builder()
                .id(recordDto.getId())
                .login(recordDto.getLogin())
                .name(recordDto.getName())
                .salary(recordDto.getSalary())
                .startDate(recordDto.getStartDate())
                .build();
    }

    public List<UserResponseDto> mapToDtos(List<UserEntity> userEntities){
        final List<UserResponseDto> recordDtoList = userEntities.stream().map(userEntity -> mapToDto(userEntity))
                .collect(Collectors.toList());
        return recordDtoList;
    }

    public UserResponseDto mapToDto(UserEntity userEntity){
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .name(userEntity.getName())
                .salary(userEntity.getSalary())
                .startDate(userEntity.getStartDate())
                .build();
    }

}
