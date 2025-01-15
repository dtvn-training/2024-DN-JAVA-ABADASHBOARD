package com.example.backend.service;

import com.google.api.services.tagmanager.model.Container;
import com.google.api.services.tagmanager.model.Workspace;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoadConfig {
    List<Container> getAllContainer();
    List<Workspace> getAllWorkspace(String containerId);
}
