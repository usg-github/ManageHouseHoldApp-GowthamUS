package com.spgroup.managehouseholdapp.service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.spgroup.managehouseholdapp.dao.ApplianceTransDAO;
import com.spgroup.managehouseholdapp.dao.FileUploadDAO;
import com.spgroup.managehouseholdapp.exceptions.ApplianceNotFoundException;
import com.spgroup.managehouseholdapp.model.ApplianceTrans;
import com.spgroup.managehouseholdapp.model.ui.Appliance;

@Service
public class ApplianceService {


	@Autowired
	private ApplianceTransDAO applianceTransDAO;
	
	@Autowired
	private FileUploadDAO fileUploadDAO;
	
	private	ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase();

	Function<ApplianceTrans, Appliance> externalToAppliance = new Function<ApplianceTrans, Appliance>() {

		public Appliance apply(ApplianceTrans applianceTrans) {
			Appliance appliance = new Appliance();
			appliance.setId(applianceTrans.getId());
			appliance.setUserId(applianceTrans.getUserId());
			appliance.setProductName(applianceTrans.getProductName());
			appliance.setProductDesc(applianceTrans.getProductDesc());
			appliance.setSerialNumber(applianceTrans.getSerialNumber());
			appliance.setBrand(applianceTrans.getBrand());
			appliance.setModel(applianceTrans.getModel());
			appliance.setStatus(applianceTrans.getStatus());
			appliance.setDateBought(applianceTrans.getDateBought().toString());
			
			
			if(applianceTrans.getImageId() != null && applianceTrans.getImageId() > 0) {
				appliance.setImageData(Base64.getEncoder().encodeToString(fileUploadDAO.getOne(applianceTrans.getImageId()).getData()));
			}
			

			return appliance;
		}
	};

	// get all users
	@GetMapping
	public List<Appliance> getAllAppliances() {
		List<ApplianceTrans> listOfAppliances = this.applianceTransDAO.findAll();
		if (!CollectionUtils.isEmpty(listOfAppliances)) {
			return listOfAppliances.stream().map(externalToAppliance).collect(Collectors.toList());
		}
		return null;
	}


	public Appliance getApplianceById( long applianceId) {
		ApplianceTrans applianceTrans = this.applianceTransDAO.findById(applianceId)
				.orElseThrow(() -> new ApplianceNotFoundException("Appliance not found with id :" + applianceId));
		return externalToAppliance.apply(applianceTrans);
	}


    @Transactional
	public void createAppliance(Appliance appliance) throws Exception {
		

		try {
			ApplianceTrans newAppliance = new ApplianceTrans();
			ApplianceTrans searchAppliance = new ApplianceTrans();
			
			newAppliance.setProductName(appliance.getProductName());
			newAppliance.setProductDesc(appliance.getProductDesc());
			newAppliance.setImageId(appliance.getImageId());
			newAppliance.setUserId(appliance.getUserId());
			
			newAppliance.setSerialNumber(appliance.getSerialNumber());
			searchAppliance.setSerialNumber(appliance.getSerialNumber());
			
			newAppliance.setBrand(appliance.getBrand());
			searchAppliance.setBrand(appliance.getBrand());
			
			newAppliance.setModel(appliance.getModel());
			searchAppliance.setModel(appliance.getModel());
			
			newAppliance.setStatus(appliance.getStatus());
			newAppliance.setCreatedDate(LocalDate.now());
			newAppliance.setUpdatedDate(LocalDate.now());
			
			
			if(StringUtils.isNotBlank(appliance.getDateBought())) {
				LocalDate date = LocalDate.parse(appliance.getDateBought());
				newAppliance.setDateBought(date);
			}
			
			Example<ApplianceTrans> applianceCheck = Example.of(searchAppliance,caseInsensitiveExampleMatcher);
			if(applianceTransDAO.findOne(applianceCheck).isPresent()) {
				throw new Exception();
			}


			
			this.applianceTransDAO.saveAndFlush(newAppliance);
		}catch(Exception e) {
			throw new Exception("Error in Saving the ApplianceRecord");
		}


	}


    @Transactional
	public void updateAppliance(Appliance appliance,long applianceId)  throws Exception {
		
		try {
			
			ApplianceTrans searchAppliance = new ApplianceTrans();
		
			ApplianceTrans updateAppliance = this.applianceTransDAO.findById(applianceId)
					.orElseThrow(() -> new ApplianceNotFoundException("User not found with id :" + applianceId));
	
			updateAppliance.setProductName(appliance.getProductName());
			updateAppliance.setProductDesc(appliance.getProductDesc());
			updateAppliance.setImageId(appliance.getImageId());
			updateAppliance.setUserId(appliance.getUserId());
			
			updateAppliance.setSerialNumber(appliance.getSerialNumber());
			searchAppliance.setSerialNumber(appliance.getSerialNumber());
			
			updateAppliance.setBrand(appliance.getBrand());
			searchAppliance.setBrand(appliance.getBrand());
			
			updateAppliance.setModel(appliance.getModel());
			searchAppliance.setModel(appliance.getModel());
			
			updateAppliance.setStatus(appliance.getStatus());
			
			
			updateAppliance.setUpdatedDate(LocalDate.now());
			
		    if(StringUtils.isNotBlank(appliance.getDateBought())) {
				LocalDate date = LocalDate.parse(appliance.getDateBought());
				updateAppliance.setDateBought(date);
		    }
			
			Example<ApplianceTrans> applianceCheck = Example.of(searchAppliance, caseInsensitiveExampleMatcher);
			ApplianceTrans existingObj = applianceTransDAO.findOne(applianceCheck).get();
			if(existingObj !=null && existingObj.getId() != updateAppliance.getId()) {
				throw new Exception();
			}
			
			this.applianceTransDAO.saveAndFlush(updateAppliance);
		}catch(Exception e) {
			throw new Exception("Error in Updating the Appliance Record");
		}
	}


	@Transactional
	public void deleteAppliance(long applianceId) {
		ApplianceTrans existingAppliance = this.applianceTransDAO.findById(applianceId)
				.orElseThrow(() -> new ApplianceNotFoundException("Appliance not found with id :" + applianceId));
		this.applianceTransDAO.delete(existingAppliance);
	}


	public List<Appliance> filterAppliance(Appliance appliance) {
	    
	    ApplianceTrans applianceTrans = new ApplianceTrans();
	    applianceTrans.setUserId(appliance.getUserId());

	    applianceTrans.setStatus(appliance.getStatus());
	    
	    if(StringUtils.isNotBlank(appliance.getSerialNumber())) {
	    	applianceTrans.setSerialNumber(appliance.getSerialNumber());	
	    }
	    
	    if(StringUtils.isNotBlank(appliance.getBrand())) {
	    	applianceTrans.setBrand(appliance.getBrand());	
	    }
	    
	    if(StringUtils.isNotBlank(appliance.getModel())) {
	    	applianceTrans.setModel(appliance.getModel());	
	    }
	    
	    if(StringUtils.isNotBlank(appliance.getStatus())) {
	    	applianceTrans.setStatus(appliance.getStatus());
	    }else {
	    	applianceTrans.setStatus(null);
	    }
    	
	    
	    if(StringUtils.isNotBlank(appliance.getDateBought())) {
		    LocalDate date = LocalDate.parse(appliance.getDateBought());
		    applianceTrans.setDateBought(date);
		    
	    }

	    ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
	    	      .withMatcher("serialNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	    	      .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	    	      .withMatcher("model", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	    	      .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase());
	    
	    Example<ApplianceTrans> example = Example.of(applianceTrans,customExampleMatcher);
	    
		List<ApplianceTrans> listOfAppliances = this.applianceTransDAO.findAll(example);
		
		if(CollectionUtils.isEmpty(listOfAppliances)) {
			throw new ApplianceNotFoundException("Appliance not found");
		} else {
			return listOfAppliances.stream().map(externalToAppliance).collect(Collectors.toList());
		}
	}


	public  List<Appliance> getSearchResults(Appliance searchInput) {
	    ApplianceTrans applianceTrans = new ApplianceTrans();
	    applianceTrans.setBrand(searchInput.getBrand());
	    applianceTrans.setSerialNumber(searchInput.getSerialNumber());
	    applianceTrans.setModel(searchInput.getModel());
	    
	    ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
	    	      .withMatcher("serialNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	    	      .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	    	      .withMatcher("model", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
	    
	    Example<ApplianceTrans> example = Example.of(applianceTrans,customExampleMatcher);
	    
	    List<ApplianceTrans> listOfAppliances = this.applianceTransDAO.findAll(example);
	    
	    if(CollectionUtils.isEmpty(listOfAppliances)) {
	    	listOfAppliances = listOfAppliances.stream().filter(appliance -> {
	    		return appliance.getUserId() ==  searchInput.getUserId();
	    	}).collect(Collectors.toList());
	    }
	    			
		if(CollectionUtils.isEmpty(listOfAppliances)) {
			throw new ApplianceNotFoundException("Appliance not found");
		} else {
			return listOfAppliances.stream().map(externalToAppliance).collect(Collectors.toList());
		}
	}
}
