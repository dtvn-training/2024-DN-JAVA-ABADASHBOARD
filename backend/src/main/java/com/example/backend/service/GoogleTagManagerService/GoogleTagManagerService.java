package com.example.backend.service.GoogleTagManagerService;

import com.example.backend.dto.request.Tag.listTagRequestGTM;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor

public class GoogleTagManagerService {
    @Value("${google-tag-manager.account_id}")
    private String accountId;
    @Value("${google-analytic.tracking-id}")
    private String trackingId;

    private final TagManager tagManager;

    public  Container findContainerName( String containerName)
            throws Exception {
        String parent = String.format("accounts/%s", accountId);
        System.out.println(accountId);
        for (Container container :
                tagManager.accounts().containers().list(parent).execute().getContainer()) {
            if (container.getName().equals(containerName)) {
                return container;
            }
        }
        throw new IllegalArgumentException("No container named Greetings in given account");
    }

    public List<Workspace> findWorkspace( String containerId) throws IOException {
        String parent = String.format("accounts/%s/containers/%s", accountId, containerId);
        return tagManager.accounts().containers().workspaces().list(parent).execute().getWorkspace();
    }


    public List<Tag> listTags(listTagRequestGTM requestGTM) throws IOException {
        // Sử dụng String.format() để tạo chuỗi 'parent' với các tham số
        String parent = String.format("accounts/%s/containers/%s/workspaces/%s", accountId, requestGTM.getContainerId(), requestGTM.getWorkspaceId());

        // Gọi phương thức list() với chuỗi 'parent' đã được tạo
        TagManager.Accounts.Containers.Workspaces.Tags.List request = tagManager
                .accounts()
                .containers()
                .workspaces()
                .tags()
                .list(parent);

        // Thực thi request và nhận phản hồi
        ListTagsResponse response = request.execute();

        if (response != null && response.getTag() != null) {
            return response.getTag();
        } else {
            return null;
        }
    }
}
