package com.example.backend.controller;

import com.example.backend.service.GoogleTagManagerService.GoogleTagManagerService;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Container;
import com.google.api.services.tagmanager.model.Tag;
import com.google.api.services.tagmanager.model.Workspace;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleTagManagerController {
    GoogleTagManagerService googleTagManagerService;
    TagManager tagManager;

    @PostMapping("/create-tag")
    public ResponseEntity<?> createTag(@RequestParam("tagName") String tagName, @RequestParam("tagType") String tagType) throws Exception {
        Container container= googleTagManagerService.findContainerName(tagManager,"abalightacademy.com");
        List<Workspace> workspaces= googleTagManagerService.findWorkspace(tagManager,container.getContainerId());
        Tag tag= googleTagManagerService.createTag(tagManager,container.getContainerId(),workspaces.getFirst().getWorkspaceId(),tagName,tagType);
        return ResponseEntity.ok().body(tag);
    }
}
