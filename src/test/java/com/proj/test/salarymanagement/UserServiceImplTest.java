package com.proj.test.salarymanagement;

import com.proj.test.salarymanagement.dto.SearchRequestDto;
import com.proj.test.salarymanagement.dto.UserRecordDto;
import com.proj.test.salarymanagement.dto.ValidationRule;
import com.proj.test.salarymanagement.entity.UserEntity;
import com.proj.test.salarymanagement.exception.ValidationException;
import com.proj.test.salarymanagement.factory.ValidationFactory;
import com.proj.test.salarymanagement.mapper.UserMapper;
import com.proj.test.salarymanagement.repository.UserRepository;
import com.proj.test.salarymanagement.service.ValidationService;
import com.proj.test.salarymanagement.service.impl.SearchRequestValidationServiceImpl;
import com.proj.test.salarymanagement.service.impl.UserServiceImpl;
import com.proj.test.salarymanagement.service.impl.UserValidationServiceImpl;
import com.proj.test.salarymanagement.utils.CSVHelper;
import com.proj.test.salarymanagement.utils.ValidationRuleHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ValidationFactory factory;

    @Mock
    private CSVHelper csvHelper;

    @Mock
    private ValidationRuleHelper ruleHelper;

    @InjectMocks
    private static UserServiceImpl userService = new UserServiceImpl();

    private static MultipartFile multipartFile;


    @Before
    public void loadCSVFile() throws IOException {
        final String resourcePath = "src/test/resources/records.csv";
        final File csvFile = new File(resourcePath);
        multipartFile = new MockMultipartFile("file",
                csvFile.getName(), "text/csv", new FileInputStream(csvFile));
    }

    // Test cases for Upload
    @Test(expected = ValidationException.class)
    public void testUploadWhenUserIdNotUnique() throws IOException {
        final List<UserRecordDto> recordDtoList = Arrays.asList(new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-01"),
                new UserRecordDto("e0002","rwesley","Ron Weasley",19234.50d,"16-Nov-01"));
        final List<String> idList = recordDtoList.stream().map(recordDto -> recordDto.getId()).collect(Collectors.toList());
        when(csvHelper.csvToUsers(ArgumentMatchers.any(InputStream.class))).thenReturn(recordDtoList);
        final List<UserEntity> userEntitiesById = Arrays.asList(new UserEntity("e0001","ssnape","Severus Snape",1234.00d,"16-Nov-01"));
        when(userRepository.findByIdIn(idList)).thenReturn(userEntitiesById);
        verify(userService.upload(multipartFile));
    }

    @Test(expected = ValidationException.class)
    public void testUploadWhenUserLoginNotUnique() throws IOException {
        final List<UserRecordDto> recordDtoList = Arrays.asList(new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-01"),
                new UserRecordDto("e0002","rwesley","Ron Weasley",19234.50d,"16-Nov-01"));
        final List<String> loginList = recordDtoList.stream().map(recordDto -> recordDto.getLogin()).collect(Collectors.toList());
        when(csvHelper.csvToUsers(ArgumentMatchers.any(InputStream.class))).thenReturn(recordDtoList);
        final List<UserEntity> userEntitiesByLogin = Arrays.asList(new UserEntity("e0003","hpotter","Harry Potter",1234.00d,"16-Nov-01"));
        when(userRepository.findByLoginIn(loginList)).thenReturn(userEntitiesByLogin);
        verify(userService.upload(multipartFile));
    }

    @Test(expected = ValidationException.class)
    public void testUploadWhenFieldsBlank() throws IOException {
        final ValidationService validationService = new UserValidationServiceImpl();
        when(factory.getService("user")).thenReturn(validationService);
        final List<UserRecordDto> recordDtoList = Arrays.asList(new UserRecordDto("e0001","hpotter",null,1234.00d,"16-Nov-2001"));
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("all","notBlank"));
        when(csvHelper.csvToUsers(any(InputStream.class))).thenReturn(recordDtoList);
        when(ruleHelper.getRules("upload")).thenReturn(validationRules);
        verify(userService.upload(multipartFile));
    }

    @Test(expected = ValidationException.class)
    public void testUploadWhenStartDateInvalid() throws IOException {
        final ValidationService validationService = new UserValidationServiceImpl();
        when(factory.getService("user")).thenReturn(validationService);
        final List<UserRecordDto> recordDtoList = Arrays.asList(new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-2001"));
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("startDate","date_format"));
        when(csvHelper.csvToUsers(any(InputStream.class))).thenReturn(recordDtoList);
        when(ruleHelper.getRules("upload")).thenReturn(validationRules);
        verify(userService.upload(multipartFile));
    }

    @Test(expected = ValidationException.class)
    public void testUploadWhenSalaryIsNegative() throws IOException {
        final ValidationService validationService = new UserValidationServiceImpl();
        when(factory.getService("user")).thenReturn(validationService);
        final List<UserRecordDto> recordDtoList = Arrays.asList(new UserRecordDto("e0001","hpotter","Harry Potter",-1234.00d,"16-Nov-2001"));
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("salary","gteq","0.0"));
        when(csvHelper.csvToUsers(any(InputStream.class))).thenReturn(recordDtoList);
        when(ruleHelper.getRules("upload")).thenReturn(validationRules);
        verify(userService.upload(multipartFile));
    }

    // Test cases for Create
    @Test(expected = ValidationException.class)
    public void testCreateWhenSalaryIsNegative(){
        final ValidationService validationService = new UserValidationServiceImpl();
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",-1234.00d,"16-Nov-2001");
        when(factory.getService("user")).thenReturn(validationService);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("salary","gteq","0.0"));
        when(ruleHelper.getRules("create")).thenReturn(validationRules);
        verify(userService.create(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testCreateWhenStartDateInvalid(){
        final ValidationService validationService = new UserValidationServiceImpl();
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",-1234.00d,"16-Nov-2050");
        when(factory.getService("user")).thenReturn(validationService);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("startDate","date_format"));
        when(ruleHelper.getRules("create")).thenReturn(validationRules);
        verify(userService.create(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testCreateWhenUserIdNotUnique(){
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-2001");
        final Optional<UserEntity> userEntity = Optional.of(new UserEntity("e0001","rwesley","Ron Weasley",19234.50d,"16-Nov-01"));
        when(userRepository.findById(userRecordDto.getId())).thenReturn(userEntity);
        verify(userService.create(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testCreateWhenUserLoginNotUnique(){
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-2001");
        final Optional<UserEntity> userEntity = Optional.of(new UserEntity("e0002","hpotter","Ron Weasley",19234.50d,"16-Nov-01"));
        when(userRepository.findByLogin(userRecordDto.getLogin())).thenReturn(userEntity);
        verify(userService.create(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testCreateWhenFieldsBlank(){
        final ValidationService validationService = new UserValidationServiceImpl();
        final UserRecordDto userRecordDto = new UserRecordDto("e0001",null,"Harry Potter",1234.00d,"16-Nov-2001");
        when(factory.getService("user")).thenReturn(validationService);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("all","notBlank"));
        when(ruleHelper.getRules("create")).thenReturn(validationRules);
        verify(userService.create(userRecordDto));
    }

    // Test cases for Update
    @Test(expected = ValidationException.class)
    public void testUpdateWhenSalaryIsNegative(){
        final ValidationService validationService = new UserValidationServiceImpl();
        final Optional<UserEntity> userEntity = Optional.of(new UserEntity("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-01"));
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",-1234.00d,"16-Nov-2001");
        when(factory.getService("user")).thenReturn(validationService);
        when(userRepository.findById(userRecordDto.getId())).thenReturn(userEntity);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("salary","gteq","0.0"));
        when(ruleHelper.getRules("update")).thenReturn(validationRules);
        verify(userService.update(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testUpdateWhenStartDateInvalid(){
        final ValidationService validationService = new UserValidationServiceImpl();
        final Optional<UserEntity> userEntity = Optional.of(new UserEntity("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-01"));
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-2001");
        when(factory.getService("user")).thenReturn(validationService);
        when(userRepository.findById(userRecordDto.getId())).thenReturn(userEntity);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("startDate","date_format"));
        when(ruleHelper.getRules("update")).thenReturn(validationRules);
        verify(userService.update(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testUpdateWhenUserIdNotPresent(){
        final Optional<UserEntity> userEntity = Optional.empty();
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-2001");
        when(userRepository.findById(userRecordDto.getId())).thenReturn(userEntity);
        verify(userService.update(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testUpdateWhenUserLoginNotUnique(){
        final Optional<UserEntity> userEntityById = Optional.of(new UserEntity("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-01"));
        final Optional<UserEntity> userEntityByLogin = Optional.of(new UserEntity("e0004","rhagrid","Rubeus Hagrid",3999.999d,"16-Nov-01"));
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","rhagrid","Harry Potter",1234.00d,"16-Nov-2001");
        when(userRepository.findById(userRecordDto.getId())).thenReturn(userEntityById);
        when(userRepository.findByLogin(userRecordDto.getLogin())).thenReturn(userEntityByLogin);
        verify(userService.update(userRecordDto));
    }

    @Test(expected = ValidationException.class)
    public void testUpdateWhenFieldsBlank(){
        final ValidationService validationService = new UserValidationServiceImpl();
        final Optional<UserEntity> userEntity = Optional.of(new UserEntity("e0001","hpotter","Harry Potter",1234.00d,"16-Nov-01"));
        final UserRecordDto userRecordDto = new UserRecordDto("e0001","hpotter","Harry Potter",null,"16-Nov-2001");
        when(factory.getService("user")).thenReturn(validationService);
        when(userRepository.findById(userRecordDto.getId())).thenReturn(userEntity);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("all","notBlank"));
        when(ruleHelper.getRules("update")).thenReturn(validationRules);
        verify(userService.update(userRecordDto));
    }

    // Test cases for Fetch
    @Test(expected = ValidationException.class)
    public void testFetchWhenMinMaxSalaryNegative(){
        final ValidationService validationService = new SearchRequestValidationServiceImpl();
        final SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setMaxSalary(-1000d);
        searchRequestDto.setMinSalary(-10d);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule(Arrays.asList("minSalary","maxSalary"),"salary"));
        when(ruleHelper.getRules("fetch")).thenReturn(validationRules);
        when(factory.getService("search")).thenReturn(validationService);
        verify(userService.fetch(searchRequestDto));
    }

    @Test(expected = ValidationException.class)
    public void testFetchWhenMinMaxSalaryEqual(){
        final ValidationService validationService = new SearchRequestValidationServiceImpl();
        final SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setMaxSalary(1000d);
        searchRequestDto.setMinSalary(1000d);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule(Arrays.asList("minSalary","maxSalary"),"salary"));
        when(ruleHelper.getRules("fetch")).thenReturn(validationRules);
        when(factory.getService("search")).thenReturn(validationService);
        verify(userService.fetch(searchRequestDto));
    }

    @Test(expected = ValidationException.class)
    public void testFetchWhenMinSalaryGreaterThanMaxSalary(){
        final ValidationService validationService = new SearchRequestValidationServiceImpl();
        final SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setMaxSalary(1000d);
        searchRequestDto.setMinSalary(10000d);
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule(Arrays.asList("minSalary","maxSalary"),"salary"));
        when(ruleHelper.getRules("fetch")).thenReturn(validationRules);
        when(factory.getService("search")).thenReturn(validationService);
        verify(userService.fetch(searchRequestDto));
    }

    // Test cases for Delete
    @Test(expected = ValidationException.class)
    public void testDeleteWhenUserIdNotPresent(){
        final String id = "e0001";
        final Optional<UserEntity> userEntityById = Optional.empty();
        when(userRepository.findById(id)).thenReturn(userEntityById);
        verify(userService.delete(id));
    }
}
