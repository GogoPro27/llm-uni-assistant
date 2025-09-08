# LLM Uni Assistant

A web application that modernizes practical exams and lab exercises with configurable LLM assistants. Professors can upload subject-specific resources (presentations, code, links, etc.) and control model behavior (token limits, prompts, temperature, etc.), while students can use the assistant during labs/exams for practical problem solving. The focus is on reducing repetitive boilerplate work, improving prompting skills, and fostering deeper understanding of concepts rather than memorization.

---

## Tech Stack
- **Backend:** Spring Boot  
- **Frontend:** React.js  
- **Database & Vector Store:** PostgreSQL, Qdrant (via Docker Compose)  

---

## Setup

1. Clone the repository:  
   ```bash
   git clone https://github.com/yourusername/emt-llm-assistant.git
   cd emt-llm-assistant
   ```

2. Start database & vector store:  
   ```bash
   docker compose up -d
   ```

3. Run backend (Spring Boot):  
   ```bash
   ./mvnw spring-boot:run
   ```

4. Run frontend (React):  
   ```bash
   cd frontend
   npm install
   npm start
   ```

---

## Docker Services

The project uses Docker Compose to manage services:

- **Postgres (Database):**
  - Runs with initialized schema and seed data.
  - Accessible on port `5432`.

- **Qdrant (Vector Store):**
  - Handles vector embeddings for LLM context.
  - Accessible on ports `6333` and `6334`.

---

## License
MIT License
