# KnowBase 🧠📚

> A production-grade, modular monolith **Retrieval-Augmented Generation (RAG)** application for intelligent document processing, tag-filtered semantic search, and AI-assisted conversational chat.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Framework-blue.svg)](https://spring.io/projects/spring-ai)
[![React](https://img.shields.io/badge/Frontend-React%20%2B%20Vite-61dafb.svg)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL%20%2B%20pgvector-blue.svg)](https://github.com/pgvector/pgvector)
[![Ollama](https://img.shields.io/badge/AI%20Engine-Ollama-black.svg)](https://ollama.com/)
[![Docker](https://img.shields.io/badge/Container-Docker-2496ed.svg)](https://www.docker.com/)

🔗 **Frontend Repository:** [Roshan-Nepal/know-base-front](https://github.com/Roshan-Nepal/know-base-front)
---

## 📌 Overview

**KnowBase** is an enterprise-ready knowledge base system built on a **modular monolith** architecture. It provides an end-to-end pipeline for parsing multi-format documents, performing text chunking and vector embedding, and enabling context-aware semantic search and AI chat over internal knowledge stores.

By incorporating **tag-based metadata filtering** alongside high-dimensional vector search in **PostgreSQL (`pgvector`)**, KnowBase delivers precise context retrieval to LLMs (via **Ollama**), minimizing hallucinations and tailoring responses to specific user domains.

---

## ✨ Key Features

- 📑 **Multi-Format Document Ingestion**: Seamless text extraction from PDFs, DOCX, TXT, and other document formats using **Apache Tika**.
- ⚡ **Event-Driven Processing**: Asynchronous document lifecycle pipeline handling parsing, chunking, and embedding generation.
- 🎯 **Metadata & Tag-Based Search**: Hybrid semantic retrieval enhanced by tag-based metadata filters for refined context selection.
- 🗄️ **Native Vector Store**: PostgreSQL with the `pgvector` extension for efficient vector similarity operations.
- 🦙 **Local & Private AI**: Powered by **Ollama** for running open-source LLMs (e.g., `llama3`) and embedding models (e.g., `nomic-embed-text`) locally without data leaks.
- 🔐 **Authentication & Security**: Dedicated authentication module managing application access and user permissions.
- 💻 **Interactive Frontend**: Modern React Single Page Application (SPA) built with Vite and React Router for seamless UI management and document chat.

---

## 🏗️ Architecture & Project Structure

KnowBase follows a **Modular Monolith** pattern to ensure high cohesion, low coupling, and easy maintenance across domain boundaries.

```txt
know-base/
├── common/             # Shared utilities, constants, exceptions, and base DTOs
├── infrastructure/     # Database configurations, security, and global cross-cutting concerns
├── auth/               # User authentication, authorization, and session management
├── document/           # Document management, Apache Tika ingestion, and chunking pipeline
├── vector/             # Vector storage abstractions, pgvector integration, and Spring AI bindings
├── ai/                 # LLM chat orchestration, RAG prompt templates, and Spring AI models

```

---

## 🛠️ Tech Stack

### **Backend**

* **Language**: Java 21
* **Framework**: Spring Boot 3.x, Spring AI
* **Parsing**: Apache Tika
* **Build Tool**: Maven / Gradle

### **AI & Data**

* **Database / Vector Database**: PostgreSQL 16+ with `pgvector` extension
* **LLM & Embeddings Engine**: Ollama (`llama3` for chat completion, `nomic-embed-text` for vector embeddings)

### **Frontend**

* **Framework**: React.js (Vite)
* **Routing**: React Router
* **State & HTTP**: Axios / Fetch API, React Hooks

### **DevOps & CI/CD**

* **Containerization**: Multi-stage Docker builds & Docker Compose
* **CI/CD**: GitHub Actions

---

## 🚀 Getting Started

### **Prerequisites**

Ensure you have the following installed locally:

* [Java 21 JDK](https://www.google.com/search?q=https://www.oracle.com/java/technologies/downloads/%23java21)
* [Node.js (v18+)](https://nodejs.org/) & `npm`
* [Docker & Docker Compose](https://www.docker.com/)
* [Ollama](https://ollama.com/) (if running AI models locally outside Docker)

---

### 🏃 Quick Start with Docker Compose (Recommended)

1. **Clone the repository**:
```bash
git clone [https://github.com/Roshan-Nepal/know-base.git](https://github.com/Roshan-Nepal/know-base.git)
cd know-base

```


2. **Spin up services using Docker Compose**:
```bash
docker compose up -d --build

```


*This starts PostgreSQL (`pgvector`), Ollama, and the KnowBase Spring Boot API container.*

3. **Download required Ollama models**:
Once the containers are up, pull the LLM and embedding models inside the `ollama` container:
```bash
docker exec -it <ollama_container_id_or_name> ollama pull llama3
docker exec -it <ollama_container_id_or_name> ollama pull nomic-embed-text

```


4. **Access the application**:
* Backend API: `http://localhost:8080`
* Frontend UI: `http://localhost:5173` (or configured frontend port)



---

### 🛠️ Manual / Local Development Setup

#### 1. Start Infrastructure Services (Database & AI Engine)

Run PostgreSQL with `pgvector` and Ollama locally:

```bash
# Start Postgres with pgvector
docker run -d --name postgres-pgvector \
  -e POSTGRES_DB=know-base \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  ankane/pgvector:v0.5.1

```

Pull Ollama models locally:

```bash
ollama pull llama3
ollama pull nomic-embed-text

```

#### 2. Configure Backend (`application.yml`)

Verify your settings in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/know-base
    username: postgres
    password: postgres
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3
      embedding:
        options:
          model: nomic-embed-text

```

#### 3. Run Backend

```bash
./mvnw clean spring-boot:run

```

#### 4. Run Frontend

Clone and start the dedicated frontend application from [know-base-front](https://github.com/Roshan-Nepal/know-base-front):

```bash
git clone [https://github.com/Roshan-Nepal/know-base-front.git](https://github.com/Roshan-Nepal/know-base-front.git)
cd know-base-front
npm install
npm run dev
```

---

## ⚙️ Configuration & Environment Variables

| Variable | Description | Default Value |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | PostgreSQL Connection JDBC URL | `jdbc:postgresql://localhost:5432/know-base` |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL User | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL Password | `postgres` |
| `SPRING_AI_OLLAMA_BASE_URL` | Ollama API Endpoint | `http://localhost:11434` |

---

## 🔄 Document Ingestion Lifecycle

1. **Upload**: Document is uploaded via the frontend or API.
2. **Parsing**: Apache Tika extracts raw text content from the uploaded document format.
3. **Chunking**: Text is split into optimal semantic token sizes.
4. **Vectorization**: Ollama (`nomic-embed-text`) generates high-dimensional vector embeddings for each chunk.
5. **Storage**: Vector embeddings and metadata (tags, file references) are written to PostgreSQL via `pgvector`.
6. **Retrieval & Chat**: User queries undergo similarity search filtered by tags, passing context into `llama3` for RAG responses.

---


## 👤 Author

Developed by **[Roshan Nepal](https://github.com/Roshan-Nepal)**.


```

```
