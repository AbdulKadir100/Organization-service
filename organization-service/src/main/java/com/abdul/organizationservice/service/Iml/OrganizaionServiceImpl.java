package com.abdul.organizationservice.service.Iml;


import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.abdul.organizationservice.dto.OrganizationDto;
import com.abdul.organizationservice.entity.Organization;
import com.abdul.organizationservice.mapper.OrganizationMapper;
import com.abdul.organizationservice.repository.OrganizationRepository;
import com.abdul.organizationservice.service.OrganizationService;

@Service
public class OrganizaionServiceImpl implements OrganizationService{
	
	private static final Logger LOGGER = Logger.getLogger(OrganizaionServiceImpl.class.getName());

	@Autowired
	private OrganizationRepository organizationRepository;
	
	
	@Override
	public OrganizationDto saveOrg(OrganizationDto organizationDto) {
	    Organization organization = OrganizationMapper.mapToOrganization(organizationDto);

	    // Uncomment the next line if you want to prevent saving an organization with an existing ID
	    // if (organizationRepository.existsById(organization.getId())) return null;

	    try {
	        Organization savedOrganization = organizationRepository.save(organization);
	        return OrganizationMapper.mapToOrganizationDto(savedOrganization);
	    } catch (DataIntegrityViolationException dive) {
	        // Handle data integrity violation (e.g., duplicate key) if needed
	        LOGGER.warning("Failed to save organization. Data integrity violation.");
	        throw new DuplicateKeyException("Organization with the same ID already exists.", dive);
	    } catch (Exception e) {
	        // Handle other potential exceptions during save
	        LOGGER.warning("Failed to save organization.");
	        throw new RuntimeException("Failed to save organization", e);
	    }
	}

	
	@Override
	public OrganizationDto getOrganizationByCode(String organizationCode) throws NotFoundException {
	    Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

	    if (organization != null) {
	        return OrganizationMapper.mapToOrganizationDto(organization);
	    } else {
	        // Handle the case where the organization with the given code is not found
	        LOGGER.warning("No organization found with code: " + organizationCode);
	        throw new NotFoundException();
	    }
	}

	@Override
	public List<OrganizationDto> getListOfOrg()throws NotFoundException{
	    List<Organization> organizations = Optional.ofNullable(organizationRepository.findAll())
	            .orElseThrow(() -> {
	                LOGGER.warning("Organization list is null.");
	                return new NotFoundException();
	            });

	    return organizations.stream()
	            .map(org -> OrganizationMapper.mapToOrganizationDto(org))
	            .collect(Collectors.toList());
	}

	@Override
	public OrganizationDto getSingleOrg(Long id) throws NotFoundException{
	    Optional<Organization> optionalOrganization = organizationRepository.findById(id);

	    Organization organization = optionalOrganization.orElseThrow(() -> {
	        LOGGER.warning("No organization found with ID: " + id);
	        return new NotFoundException();
	    });

	    return OrganizationMapper.mapToOrganizationDto(organization);
	}


	@Override
	public OrganizationDto update(OrganizationDto organizationDto) {
		OrganizationDto savedOrgDto=null;
		try {
	        Optional<Organization> optionalOrganization = organizationRepository.findById(organizationDto.getId());

	        if (optionalOrganization.isPresent()) {
	            Organization organization = optionalOrganization.get();

	            // Update organization properties
	            organization.setOrganizationName(organizationDto.getOrganizationName());
	            organization.setOrganizationDescription(organizationDto.getOrganizationDescription());
	            organization.setOrganizationCode(organizationDto.getOrganizationCode());
	            organization.setCreatedDate(organizationDto.getCreatedDate());

	            // Save and return the updated organization
	            Organization updatedOrganization = organizationRepository.save(organization);
	            savedOrgDto= OrganizationMapper.mapToOrganizationDto(updatedOrganization);
	        } else {
	            // Handle the case where the organization with the given ID is not found
	            throw new Exception("Organization not found with ID: " + organizationDto.getId());
	        }
	    } catch (Exception e) {
	        // Handle other potential exceptions (e.g., DataAccessException, RuntimeException)
	        //throw new Exception("Failed to update organization", e);
	    }
	    return  savedOrgDto;
	}



	@Override
	public void delete(Long id) {
	    Optional<Organization> optionalOrganization = organizationRepository.findById(id);
	    
	    optionalOrganization.ifPresent(organization -> {
	        organizationRepository.delete(organization);
	        LOGGER.info("Organization with ID " + id + " deleted successfully.");
	    });

	    // If organization is not present, log a message
	    if (optionalOrganization.isEmpty()) {
	        LOGGER.warning("No organization found with ID: " + id);
	    }
	}

	
	
}