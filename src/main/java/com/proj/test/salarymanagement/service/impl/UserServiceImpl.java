package com.proj.test.salarymanagement.service.impl;

import com.proj.test.salarymanagement.dto.*;
import com.proj.test.salarymanagement.entity.UserEntity;
import com.proj.test.salarymanagement.exception.UserException;
import com.proj.test.salarymanagement.exception.ValidationException;
import com.proj.test.salarymanagement.factory.ValidationFactory;
import com.proj.test.salarymanagement.mapper.UserMapper;
import com.proj.test.salarymanagement.repository.UserRepository;
import com.proj.test.salarymanagement.service.UserService;
import com.proj.test.salarymanagement.service.ValidationService;
import com.proj.test.salarymanagement.specification.UserSpecification;
import com.proj.test.salarymanagement.utils.CSVHelper;
import com.proj.test.salarymanagement.utils.Constants;
import com.proj.test.salarymanagement.utils.ValidationRuleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.proj.test.salarymanagement.utils.Constants.CODE_201;

@Service
public class UserServiceImpl implements UserService {

    final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ValidationFactory factory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CSVHelper csvHelper;

    @Autowired
    private ValidationRuleHelper ruleHelper;
    @Override
    public ResponseDto upload(MultipartFile file) throws IOException {
        if(CSVHelper.hasCSVFormat(file)){
            final List<UserRecordDto> recordDtoList = csvHelper.csvToUsers(file.getInputStream());
            final List<String> idList = recordDtoList. stream().map(recordDto -> recordDto.getId()).collect(Collectors.toList());
            final List<UserEntity> userEntitiesById = userRepository.findByIdIn(idList);
            if(Objects.nonNull(userEntitiesById) && !userEntitiesById.isEmpty())
                throw new ValidationException("Employee ID already exists");
            final List<String> loginList = recordDtoList.stream().map(recordDto -> recordDto.getLogin()).collect(Collectors.toList());
            final List<UserEntity> userEntitiesByLogin = userRepository.findByLoginIn(loginList);
            if(Objects.nonNull(userEntitiesByLogin) && !userEntitiesByLogin.isEmpty())
                throw new ValidationException("Employee Login not unique");
            final List<ValidationRule> validationRules = ruleHelper.getRules("upload");
            final ValidationService validationService = factory.getService("user");
            validationService.validate(recordDtoList,validationRules);
            userRepository.saveAll(userMapper.mapToEntities(recordDtoList));
            logger.info("Records Uploaded Successfully : {}",recordDtoList.size());
            return new ResponseDto(Constants.CODE_200, Constants.DATA_UPLOADED_SUCCESSFULLY);
        }
        throw new UserException("Upload Failed with exceptions. Please Check Log.");
    }

    @Override
    @Transactional
    public ResponseDto create(UserRecordDto recordDto) {
        // Check Employee ID already present or not
        final Optional<UserEntity> userDbEntityByEmpId = userRepository.findById(recordDto.getId());
        if(Objects.nonNull(userDbEntityByEmpId) && userDbEntityByEmpId.isPresent())
            throw new ValidationException("Employee ID already exists");

        // Check Employee login already present or not
        final Optional<UserEntity> userDbEntityByLogin = userRepository.findByLogin(recordDto.getLogin());
        if(Objects.nonNull(userDbEntityByLogin) && userDbEntityByLogin.isPresent())
            throw new ValidationException("Employee Login not unique");

        final List<ValidationRule> validationRules = ruleHelper.getRules("create");
        final ValidationService validationService = factory.getService("user");
        validationService.validate(recordDto,validationRules);
        final UserEntity userEntity = userMapper.mapToEntity(recordDto);
        userRepository.save(userEntity);
        final UserResponseDto userResponseDto = userMapper.mapToDto(userEntity);
        logger.info("Record Created Successfully");
        return new ResponseDto(CODE_201, Constants.NEW_EMPLOYEE_RECORD_CREATED,userResponseDto);
    }

    @Override
    @Transactional
    public ResponseDto fetch(SearchRequestDto searchRequestDto) {
        final List<ValidationRule> validationRules = ruleHelper.getRules("fetch");
        final ValidationService validationService = factory.getService("search");
        validationService.validate(searchRequestDto,validationRules);
        final Pageable pageable = PageRequest.of(searchRequestDto.getOffset(), searchRequestDto.getLimit() == 0 ? 10 : searchRequestDto.getLimit(),
                                  createSortingCriteria(searchRequestDto.getSortParams()));
        final SearchFilter salaryMinFilter = new SearchFilter("salary","GREATER_THAN_EQUAL",String.valueOf(searchRequestDto.getMinSalary()));
        final SearchFilter salaryMaxFilter = new SearchFilter("salary","LESS_THAN",String.valueOf(searchRequestDto.getMaxSalary()));
        final List<SearchFilter> filters = Objects.isNull(searchRequestDto.getFilters()) ? new ArrayList<>(): searchRequestDto.getFilters();
        filters.add(salaryMinFilter);
        filters.add(salaryMaxFilter);
        final Page<UserEntity> entityPage = userRepository.findAll(createFilterSpecification(filters),pageable);
        final List<UserEntity> userEntities = entityPage.getContent();
        final List<UserResponseDto> userResponseDtoList = userMapper.mapToDtos(userEntities);
        logger.info(Constants.EMPLOYEE_RECORDS_FETCHED_SUCCESSFULLY);
        return new ResponseDto(Constants.CODE_200, Constants.EMPLOYEE_RECORDS_FETCHED_SUCCESSFULLY, userResponseDtoList);
    }

    private UserSpecification createFilterSpecification(final List<SearchFilter> filterList){
        final UserSpecification userSpecification = new UserSpecification(filterList);
        return userSpecification;
    }
    private Sort createSortingCriteria(final List<SortParam> sortParams){
        Sort sort = null;
        if(Objects.nonNull(sortParams) && !sortParams.isEmpty()) {
            for (SortParam sortParam : sortParams) {
                if (Objects.isNull(sort))
                    sort = Sort.by("asc".equals(sortParam.getOrder()) ? Sort.Direction.ASC : Sort.Direction.DESC, sortParam.getName());
                else
                    sort.and(Sort.by("asc".equals(sortParam.getOrder()) ? Sort.Direction.ASC : Sort.Direction.DESC, sortParam.getName()));
            }
        }
        if(Objects.isNull(sort))
            return Sort.by(Sort.Direction.ASC,"id");
        return sort;
    }



    @Override
    @Transactional
    public ResponseDto update(UserRecordDto userRecordDto) {
        final Optional<UserEntity> userDbEntityByEmpId = userRepository.findById(userRecordDto.getId());
        if(Objects.isNull(userDbEntityByEmpId) || !userDbEntityByEmpId.isPresent())
            throw new ValidationException("No such Employee");
        final UserEntity userEntity = userDbEntityByEmpId.get();
        if(!userEntity.getLogin().equals(userRecordDto.getLogin())){
            final Optional<UserEntity> userDbEntityByLogin = userRepository.findByLogin(userRecordDto.getLogin());
            if(Objects.nonNull(userDbEntityByLogin) && userDbEntityByLogin.isPresent())
                throw new ValidationException("Employee Login not unique");
        }
        final List<ValidationRule> validationRules = ruleHelper.getRules("update");
        final ValidationService validationService = factory.getService("user");
        validationService.validate(userRecordDto,validationRules);
        updateValues(userEntity,userRecordDto);
        userRepository.save(userEntity);
        final UserResponseDto userResponseDto = userMapper.mapToDto(userEntity);
        logger.info(Constants.EMPLOYEE_RECORD_SUCCESSFULLY_UPDATED);
        return new ResponseDto(Constants.CODE_200, Constants.EMPLOYEE_RECORD_SUCCESSFULLY_UPDATED,userResponseDto);
    }

    @Override
    @Transactional
    public ResponseDto delete(String id) {
        final Optional<UserEntity> userDbEntityByEmpId = userRepository.findById(id);
        if(Objects.isNull(userDbEntityByEmpId) || !userDbEntityByEmpId.isPresent())
            throw new ValidationException("No such Employee");
        userRepository.deleteById(id);
        logger.info(Constants.EMPLOYEE_SUCCESSFULLY_DELETED);
        return new ResponseDto(Constants.CODE_200, Constants.EMPLOYEE_SUCCESSFULLY_DELETED);
    }

    private void updateValues(final UserEntity userEntity, final UserRecordDto recordDto){
        userEntity.setLogin(recordDto.getLogin());
        userEntity.setName(recordDto.getName());
        userEntity.setSalary(recordDto.getSalary());
        userEntity.setStartDate(recordDto.getStartDate());
    }
}
