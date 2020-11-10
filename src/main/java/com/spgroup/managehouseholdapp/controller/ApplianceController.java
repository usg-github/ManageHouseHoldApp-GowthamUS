package com.spgroup.managehouseholdapp.controller;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spgroup.managehouseholdapp.model.ApplianceTrans;
import com.spgroup.managehouseholdapp.model.ui.Appliance;
import com.spgroup.managehouseholdapp.service.ApplianceService;

@RestController
@RequestMapping("/appliance")
public class ApplianceController {

	@Autowired
	private ApplianceService applianceTransService;

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

			return appliance;
		}
	};

	// get all users
	@GetMapping
	public List<Appliance> getAllAppliances() {
		return this.applianceTransService.getAllAppliances();
	}

	// get Appliance by id
	@GetMapping("/{id}")
	public Appliance getApplianceById(@PathVariable(value = "id") long applianceId) {
		return this.applianceTransService.getApplianceById(applianceId);
	}
	
	// get Appliance by id
	@PostMapping("/search")
	public List<Appliance> search(@RequestBody Appliance appliance) {
		return this.applianceTransService.getSearchResults(appliance);
	}
	
	// Filter Appliance
	@PostMapping("/filter")
	public List<Appliance> filterAppliance(@RequestBody Appliance appliance) throws Exception {
		return this.applianceTransService.filterAppliance(appliance);
	}

	// create Appliance
	@PostMapping("/new")
	public void createAppliance(@RequestBody Appliance appliance) throws Exception {
		this.applianceTransService.createAppliance(appliance);
	}

	// update Appliance
	@PutMapping("/{id}")
	public void updateAppliance(@RequestBody Appliance appliance, @PathVariable("id") long applianceId)  throws Exception {
		this.applianceTransService.updateAppliance(appliance, applianceId);
	}

	// delete Appliance by id
	@DeleteMapping("/{id}")
	public void deleteAppliance(@PathVariable("id") long applianceId) {
		this.applianceTransService.deleteAppliance(applianceId);
	}
}