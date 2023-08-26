package com.naipy.alpha.modules.localization.repository;

import com.naipy.alpha.modules.localization.model.Localization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalizationRepository extends JpaRepository<Localization, Long> {

}
