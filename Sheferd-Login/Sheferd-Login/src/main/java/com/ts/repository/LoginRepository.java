package com.ts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ts.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

}
