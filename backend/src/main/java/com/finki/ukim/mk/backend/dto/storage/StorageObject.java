package com.finki.ukim.mk.backend.dto.storage;

public record StorageObject(String uri, String checksumSha256, long sizeBytes) {
}