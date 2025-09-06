package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.dto.storage.StorageObject;
import com.finki.ukim.mk.backend.service.domain.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Component
public class StorageServiceImpl implements StorageService {

  private final Path root;

  public StorageServiceImpl(@Value("${app.storage.root}") String rootDir) throws IOException {
    this.root = Paths.get(rootDir).toAbsolutePath().normalize();
    Files.createDirectories(this.root);
  }

  @Override
  public StorageObject save(InputStream in, String originalFilename, String mimeType, long sizeHint) throws IOException, NoSuchAlgorithmException {
    String ext = safeExt(originalFilename);
    String key = datePrefix() + "/" + UUID.randomUUID() + ext;

    Path temp = Files.createTempFile(this.root, "up_", ".part");
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    long bytes;
    try (DigestInputStream din = new DigestInputStream(in, md);
         OutputStream out = Files.newOutputStream(temp, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
      bytes = din.transferTo(out);
    }

    String checksum = HexFormat.of().formatHex(md.digest());

    Path finalPath = this.root.resolve(key);
    Files.createDirectories(finalPath.getParent());
    Files.move(temp, finalPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

    String uri = finalPath.toUri().toString();
    return new StorageObject(uri, checksum, bytes);
  }

  @Override
  public Optional<InputStream> open(String uri) throws URISyntaxException, IOException {
    Path p = Paths.get(new java.net.URI(uri));
    return Files.exists(p) ? Optional.of(Files.newInputStream(p, StandardOpenOption.READ)) : Optional.empty();
  }

  @Override
  public void delete(String uri) throws URISyntaxException, IOException {
    Path p = Paths.get(new java.net.URI(uri));
    Files.deleteIfExists(p);
  }

  private static String datePrefix() {
    LocalDate d = LocalDate.now();
    return "%04d/%02d/%02d".formatted(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
  }

  private static String safeExt(String name) {
    if (name == null) return "";
    int dot = name.lastIndexOf('.');
    if (dot < 0 || dot == name.length() - 1) return "";
    String ext = name.substring(dot).toLowerCase();
    return ext.matches("\\.(pdf|ppt|pptx|doc|docx|xls|xlsx|png|jpg|jpeg)") ? ext : "";
  }
}