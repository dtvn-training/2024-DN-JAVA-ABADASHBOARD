package com.example.backend.service;

import java.io.IOException;
import java.util.List;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.google.api.services.tagmanager.model.Tag;

public interface GoogleTagManagerService {
    List<Tag> listTags(ListTagRequestGTM requestGTM) throws IOException;
}
