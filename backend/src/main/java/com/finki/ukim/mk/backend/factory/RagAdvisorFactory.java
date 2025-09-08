package com.finki.ukim.mk.backend.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagAdvisorFactory {
  private final VectorStore vectorStore;

  private final PromptTemplate relaxedTemplate = new PromptTemplate("""
    Here is some additional reference material that may or may not be relevant:
    
    ---------------------
    {context}
    ---------------------
    
    User question:
    {query}
    
    Use the reference material when it helps, but you may also use your general knowledge.
    If you use the reference, incorporate it naturally (no rigid "based on the context" phrasing).
    """);

  public RetrievalAugmentationAdvisor build(Integer topK,
                                            Double similarity,
                                            Boolean allowEmpty,
                                            Boolean relaxed) {
    VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
      .vectorStore(vectorStore)
      .topK(topK)
      .similarityThreshold(similarity)
      .build();

    ContextualQueryAugmenter.Builder augmenterBuilder = ContextualQueryAugmenter.builder()
      .allowEmptyContext(allowEmpty);

    ContextualQueryAugmenter augmenter = relaxed ? augmenterBuilder.promptTemplate(relaxedTemplate).build() : augmenterBuilder.build();

    return RetrievalAugmentationAdvisor.builder()
      .documentRetriever(retriever)
      .queryAugmenter(augmenter)
      .build();
  }
}