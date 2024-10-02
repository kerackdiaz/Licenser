package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    List<CompanyExtractDTO> getAllCompanies();

    CompanyDTO currentCompany();

    Map<String, Object> CreateCompany(RegisterCompanyDTO register);

    String updateCompany(String id, CompanyDTO companyDTO);
}
