package com.assignment.projectmanagementsystem.controller;


import com.assignment.projectmanagementsystem.exception.ProjectNotFoundException;
import com.assignment.projectmanagementsystem.model.Project;
import com.assignment.projectmanagementsystem.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Project> findProjectById(@PathVariable Long id) throws ProjectNotFoundException{
        Project projectById = projectService.findProject(id);
        return ResponseEntity.ok(projectById);
    }

    @GetMapping("/get/all")
    public List<Project> findProjectByAll() {
        return projectService.findAllProject();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable Long id) throws ProjectNotFoundException{
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,@Valid @RequestBody Project project) throws ProjectNotFoundException {
        Project updatedProject = projectService.updateProject(id,project);
        return ResponseEntity.ok(updatedProject);
    }
}
