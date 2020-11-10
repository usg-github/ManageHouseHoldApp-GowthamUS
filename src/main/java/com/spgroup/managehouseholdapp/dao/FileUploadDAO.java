package com.spgroup.managehouseholdapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spgroup.managehouseholdapp.model.FileUpload;

@Repository
public interface FileUploadDAO extends JpaRepository<FileUpload, Long>{

}
