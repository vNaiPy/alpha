package com.naipy.alpha.modules.exceptions.repository;

import com.naipy.alpha.modules.exceptions.models.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorMessage, String> {
}
