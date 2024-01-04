package com.abdul.organizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdul.organizationservice.dto.OrganizationDto;
import com.abdul.organizationservice.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>{
	Organization findByOrganizationCode(String organizationCode);

}
