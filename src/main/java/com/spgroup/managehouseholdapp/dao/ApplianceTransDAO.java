package com.spgroup.managehouseholdapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spgroup.managehouseholdapp.model.ApplianceTrans;

@Repository
public interface ApplianceTransDAO extends JpaRepository<ApplianceTrans, Long>{

}
