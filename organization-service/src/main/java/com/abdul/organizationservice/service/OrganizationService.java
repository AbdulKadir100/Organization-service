package com.abdul.organizationservice.service;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.abdul.organizationservice.dto.OrganizationDto;

public interface OrganizationService {

    /**
     * Saves an organization.
     * 
     * @param organizationDto The organization DTO to be saved.
     * @return The saved organization DTO.
     */
    OrganizationDto saveOrg(OrganizationDto organizationDto);

    /**
     * Retrieves a single organization by ID.
     * 
     * @param id The ID of the organization to retrieve.
     * @return The organization DTO.
     * @throws NotFoundException If the organization is not found.
     */
    OrganizationDto getSingleOrg(Long id) throws NotFoundException;

    /**
     * Retrieves an organization by its code.
     * 
     * @param organizationCode The code of the organization to retrieve.
     * @return The organization DTO.
     * @throws NotFoundException If the organization is not found.
     */
    OrganizationDto getOrganizationByCode(String organizationCode) throws NotFoundException;

    /**
     * Retrieves a list of all organizations.
     * 
     * @return The list of organization DTOs.
     * @throws NotFoundException If no organizations are found.
     */
    List<OrganizationDto> getListOfOrg() throws NotFoundException;

    /**
     * Updates an organization.
     * 
     * @param organizationDto The organization DTO with updated information.
     * @return The updated organization DTO.
     */
    OrganizationDto update(OrganizationDto organizationDto);

    /**
     * Deletes an organization by ID.
     * 
     * @param id The ID of the organization to delete.
     */
    void delete(Long id);
}
