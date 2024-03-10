package com.assignment.projectmanagementsystem.service;

import com.assignment.projectmanagementsystem.exception.ProjectNotFoundException;
import com.assignment.projectmanagementsystem.model.Project;
import com.assignment.projectmanagementsystem.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@Service
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;


    @Test
    void testCreateProject() {
        Project project = new Project();
        project.setName("Project for testing");
        project.setDescription("Unit testing");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusMonths(3));

        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project savedProject = projectService.createProject(project);
        Mockito.verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(project, savedProject);
    }

    @Test
    void testFindProjectById() throws ProjectNotFoundException {
        Project project = new Project(1L, "Project A", "Description A", LocalDate.now(), LocalDate.now().plusMonths(1));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project foundProject = projectService.findProject(1L);
        Mockito.verify(projectRepository, Mockito.times(1)).findById(1L);
        assertEquals(project, foundProject);
    }

    @Test
    void testFindAllProjects() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1L, "Project A", "Description A", LocalDate.now(), LocalDate.now().plusMonths(1)));
        projects.add(new Project(2L, "Project B", "Description B", LocalDate.now(), LocalDate.now().plusMonths(2)));
        when(projectRepository.findAll()).thenReturn(projects);
        List<Project> allProjects = projectService.findAllProject();
        Mockito.verify(projectRepository, Mockito.times(1)).findAll();
        assertEquals(projects, allProjects);
    }

    @Test
    void testUpdateProject() throws ProjectNotFoundException {
        Project existingProject = new Project(1L, "Existing Project", "Description", LocalDate.now(), LocalDate.now().plusMonths(1));
        Project updatedProjectData = new Project(1L, "Updated Project", "Updated Description", LocalDate.now(), LocalDate.now().plusMonths(2));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProjectData);
        Project updatedProject = projectService.updateProject(1L, updatedProjectData);
        Mockito.verify(projectRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(projectRepository, Mockito.times(1)).save(updatedProjectData);
        assertEquals(updatedProjectData, updatedProject);
    }

    @Test
    void testUpdateProjectNotFound() {
        Project updatedProjectData = new Project(1L, "Updated Project", "Updated Description", LocalDate.now(), LocalDate.now().plusMonths(2));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(1L, updatedProjectData));
        Mockito.verify(projectRepository, Mockito.times(1)).findById(1L);
    }

}
