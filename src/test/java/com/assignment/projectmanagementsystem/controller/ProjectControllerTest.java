package com.assignment.projectmanagementsystem.controller;


import com.assignment.projectmanagementsystem.ProjectManagementSystemApplication;
import com.assignment.projectmanagementsystem.exception.ProjectNotFoundException;
import com.assignment.projectmanagementsystem.model.Project;
import com.assignment.projectmanagementsystem.repository.ProjectRepository;
import com.assignment.projectmanagementsystem.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectManagementSystemApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectControllerTest {

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
    void testCreateProject() throws Exception {
        Project project = new Project(1L, "Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusMonths(1));

        when(projectService.createProject(any(Project.class))).thenReturn(project);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
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

    @Test
    void testDeleteProject() throws Exception {
        Long projectId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/project/delete/{id}", projectId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
