package com.proj.test.salarymanagement.repository;

import com.proj.test.salarymanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByLogin(String login);

    List<UserEntity> findByIdIn(List<String> idList);

    List<UserEntity> findByLoginIn(List<String> loginList);
}
