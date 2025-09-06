package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.service.domain.VectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VectorStoreServiceImpl implements VectorStoreService {
  private final VectorStore vectorStore;

  public void storeDocument(Resource resource, Long groupSubjectId) {
    log.info("Loading File {} as Document", resource.getFilename());

    DocumentReader pdfReader =
      new PagePdfDocumentReader(resource,
        PdfDocumentReaderConfig.builder()
          .build());

    log.info("Splitting document's text");
    List<Document> documents = pdfReader.get();

    documents.forEach(doc -> addResourcePathToDocMetadata(doc, groupSubjectId));

    List<Document> documentSplits = new TokenTextSplitter()
      .split(documents);

    log.info("Creating and storing Embeddings from Documents");
    vectorStore.add(documentSplits);
  }

  @SneakyThrows
  private void addResourcePathToDocMetadata(Document doc, Long groupSubjectId) {
    doc.getMetadata().put("group_id", String.valueOf(groupSubjectId));
  }
}
