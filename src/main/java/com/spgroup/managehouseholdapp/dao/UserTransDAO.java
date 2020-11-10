package com.spgroup.managehouseholdapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spgroup.managehouseholdapp.model.UserTrans;

@Repository
public interface UserTransDAO extends JpaRepository<UserTrans, Long>{

}
