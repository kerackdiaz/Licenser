package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.ProjectDTO;
import jakarta.mail.MessagingException;

import java.util.Map;
import java.util.UUID;

public interface ProjectService {


    String newProject(ProjectDTO project) throws MessagingException;

    String renewProject(Long projectId) throws MessagingException;

    String updateProject(Long projectId, ProjectDTO projectDTO);

    ProjectDTO getProject(Long id);
}
