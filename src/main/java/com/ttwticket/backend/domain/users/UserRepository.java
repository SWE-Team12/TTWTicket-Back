package com.ttwticket.backend.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserIdAndIsDeleted(Integer userId, Boolean isDeleted);

    User findByEmailAndIsDeleted(String email, Boolean isDeleted);


}
