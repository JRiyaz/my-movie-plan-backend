package com.MyMoviePlan.repository;

import com.sun.deploy.xml.GeneralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GeneralEntity, Integer> {
}
