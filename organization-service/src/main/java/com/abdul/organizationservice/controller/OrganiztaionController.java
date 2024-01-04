package com.abdul.organizationservice.controller;


import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdul.organizationservice.dto.OrganizationDto;

import com.abdul.organizationservice.service.OrganizationService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/organizations")
@AllArgsConstructor
public class OrganiztaionController {
	
	private static final Logger LOGGER = Logger.getLogger(OrganiztaionController.class.getName());
	
	@Autowired
	private OrganizationService organizationService;
	
	@PostMapping
	public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto) {
	    try {
	        OrganizationDto savedOrganizationDto = organizationService.saveOrg(organizationDto);
	        return new ResponseEntity<>(savedOrganizationDto, HttpStatus.CREATED);
	    } catch (DuplicateKeyException dke) {
	        // Handle the case where the organization with the same ID already exists
	        return new ResponseEntity<>(HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        // Handle other potential exceptions
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/code/{code}")
	public ResponseEntity<OrganizationDto> OrganizationByGivenCode(@PathVariable(name = "code") String organizationCode) {
	    try {
	        OrganizationDto organizationDto = organizationService.getOrganizationByCode(organizationCode);
	        return new ResponseEntity<>(organizationDto, HttpStatus.OK);
	    } catch (NotFoundException e) {
	        // Log the exception and return a 404 status code
	        LOGGER.warning("Organization not found with code: " + organizationCode);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        // Log other potential exceptions and return a 500 status code
	        LOGGER.warning("Error while retrieving organization by code: " + organizationCode);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrganizationDto> SingleOrganizationByGivenId(@PathVariable(name = "id") Long id) {
	    try {
	        OrganizationDto organizationDto = organizationService.getSingleOrg(id);
	        return ResponseEntity.ok(organizationDto);
	    } catch (NotFoundException e) {
	        // Log the exception and return a 404 status code
	        LOGGER.warning("Organization not found with ID: " + id);
	        return ResponseEntity.notFound().build();
	    } catch (Exception e) {
	        // Log other potential exceptions and return a 500 status code
	        LOGGER.warning("Error while retrieving organization by ID: " + id);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	
	@GetMapping
	public ResponseEntity<List<OrganizationDto>> getAllOrganizationsList() {
	    try {
	        List<OrganizationDto> organizations = organizationService.getListOfOrg();

	        if (organizations.isEmpty()) {
	            // Log a message and return a 404 status code
	            LOGGER.warning("No organizations found.");
	            return ResponseEntity.notFound().build();
	        }

	        return ResponseEntity.ok(organizations);
	    } catch (Exception e) {
	        // Log other potential exceptions and return a 500 status code
	        LOGGER.warning("Error while retrieving organizations list.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	
}