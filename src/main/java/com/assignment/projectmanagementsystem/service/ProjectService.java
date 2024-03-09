package com.assignment.projectmanagementsystem.service;

import com.assignment.projectmanagementsystem.exception.ProjectNotFoundException;
import com.assignment.projectmanagementsystem.model.Project;
import com.assignment.projectmanagementsystem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        Project savedproject = projectRepository.save(project);
        return savedproject;
    }

    public Project findProject(Long id) throws ProjectNotFoundException{
        Optional<Project> findById = projectRepository.findById(id);
        if (!findById.isPresent()){
            throw new ProjectNotFoundException(String.format("Project not found with id %s", id));
        }
        return findById.get();
    }

    public List<Project> findAllProject() {
        return projectRepository.findAll();
    }

    public void deleteProject(Long id) throws ProjectNotFoundException{
        Optional<Project> findById = projectRepository.findById(id);
        Project project = findById.orElseThrow(() -> new ProjectNotFoundException(String.format("Project not Found with id %s", id)));
        projectRepository.delete(project);
    }

    public Project updateProject(Long id, Project project) throws ProjectNotFoundException {

        Optional<Project> optionalProject = projectRepository.findById(id);
        if (!optionalProject.isPresent()) {
            throw new ProjectNotFoundException(String.format("Project not found with id %s", id));
        }
        Project updatedProject = optionalProject.get();
        updatedProject.setName(project.getName());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setStartDate(project.getStartDate());
        updatedProject.setEndDate(project.getEndDate());

        projectRepository.save(updatedProject);
        return updatedProject;
    }
}
