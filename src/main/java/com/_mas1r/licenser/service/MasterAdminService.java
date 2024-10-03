package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MasterAdminService {

    void generateAndStoreKeys() throws NoSuchAlgorithmException;

    List<CompanyExtractDTO> getAllCompanies(String publicKey);

    Map<String, Object> createNewCompany(String publicKey, RegisterCompanyDTO register);

    Map<String, Object> changeStatusCompany(String publicKey, UUID companyId);

    Map<String, Object> renewalLicense(String publicKey, UUID companyId, LicenseCompanyStatus licenseStatus);
}
