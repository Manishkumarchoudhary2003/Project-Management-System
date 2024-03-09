package com.assignment.projectmanagementsystem;

import com.assignment.projectmanagementsystem.exception.ProjectNotFoundException;
import com.assignment.projectmanagementsystem.model.Project;
import com.assignment.projectmanagementsystem.repository.ProjectRepository;
import com.assignment.projectmanagementsystem.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProjectManagementSystemApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Project> projects;

    @BeforeEach
    void setUp() {
        projects = new ArrayList<>();
        projects.add(new Project(1L, "Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusMonths(1)));
        projects.add(new Project(2L, "Project 2", "Description 2", LocalDate.now(), LocalDate.now().plusMonths(1)));
    }

    @Test
    void testCreateProjectService() {
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
    void testFindProjectByIdService() throws ProjectNotFoundException {
        Project project = new Project(1L, "Project A", "Description A", LocalDate.now(), LocalDate.now().plusMonths(1));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project foundProject = projectService.findProject(1L);
        Mockito.verify(projectRepository, Mockito.times(1)).findById(1L);
        assertEquals(project, foundProject);
    }

    @Test
    void testFindAllProjectsService() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1L, "Project A", "Description A", LocalDate.now(), LocalDate.now().plusMonths(1)));
        projects.add(new Project(2L, "Project B", "Description B", LocalDate.now(), LocalDate.now().plusMonths(2)));
        when(projectRepository.findAll()).thenReturn(projects);
        List<Project> allProjects = projectService.findAllProject();
        Mockito.verify(projectRepository, Mockito.times(1)).findAll();
        assertEquals(projects, allProjects);
    }

    @Test
    void testUpdateProjectService() throws ProjectNotFoundException {
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


    @Test
    void testCreateProject() throws Exception {
        // Create a project object
        Project project = new Project(1L, "Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusMonths(1));

        when(projectService.createProject(any(Project.class))).thenReturn(project);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project))) // Convert object to JSON string
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

    }

    @Test
    void testFindProjectById() throws Exception {
        Long projectId = 1L;
        Project project = new Project(projectId, "Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusMonths(1));

        when(projectService.findProject(projectId)).thenReturn(project);

        mockMvc.perform(MockMvcRequestBuilders.get("/project/get/{id}", projectId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(projectId));
    }

    @Test
    void testFindProjectByAll() throws Exception {
        when(projectService.findAllProject()).thenReturn(projects);

        mockMvc.perform(MockMvcRequestBuilders.get("/project/get/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(projects.get(0).getId()));
    }

    @Test
    void testDeleteProject() throws Exception {
        Long projectId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/project/delete/{id}", projectId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testUpdateProject() throws Exception {
        Long projectId = 1L;
        Project updatedProject = new Project(projectId, "Updated Project", "Updated Description", LocalDate.now(), LocalDate.now().plusMonths(1));

        when(projectService.updateProject(projectId, updatedProject)).thenReturn(updatedProject);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/project/update/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Project\",\"description\":\"Updated Description\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }
}
