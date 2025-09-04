package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.dto.storage.StorageObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface StorageService {
  StorageObject save(InputStream in, String originalFilename, String mimeType, long sizeHint) throws IOException, NoSuchAlgorithmException;
  Optional<InputStream> open(String uri) throws URISyntaxException, IOException;
  void delete(String uri) throws URISyntaxException, IOException;
}