package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProjectExtractDTO {

    private String id;

    private String clientName;

    private String projectName;

    private String license;

    private LocalDate initDate;

    private LocalDate ExpDate;

    private String licenseType;

    private String projectStatus;

    public ProjectExtractDTO(Project project) {
        this.id = String.valueOf(project.getId());
        this.clientName = project.getClientName();
        this.projectName = project.getProjectName();
        this.license = project.isStatusLicense() ? "Active" : "Inactive";
        this.initDate = project.getInitDate();
        this.ExpDate = project.getExpDate();
        this.licenseType = project.getLicenseType().toString();
        this.projectStatus = project.isStatusProject() ? "Active" : "Inactive";
    }

}
