# 🤖 Bot Intelligent - Chatbot Full-Stack avec IA

Application web de chatbot intelligent polyvalent intégrant l'API OpenAI pour des réponses contextuelles et naturelles.

## 🚀 Technologies

### Backend
- **Java 21** & **Spring Boot 4**
- **Spring Data JPA** / Hibernate
- **PostgreSQL**
- **WebSocket** / STOMP
- **OpenAI API** (GPT-4o-mini)
- **Maven**

### Frontend (Phase 3 - À venir)
- **Angular 17+**
- **TypeScript**
- **Angular Material**
- **RxJS**

## ✨ Fonctionnalités

### Backend (Phase 2 - ✅ Terminée)

- ✅ **API REST complète** : Gestion utilisateurs, conversations, messages
- ✅ **WebSocket temps réel** : Communication bidirectionnelle instantanée
- ✅ **Bot intelligent multi-niveaux** :
  - Analyse sémantique (normalisation synonymes, détection sentiment)
  - Gestion contexte conversationnel (5 derniers messages)
  - Base de connaissances locale (30+ FAQ)
  - Intégration OpenAI GPT-4o-mini
  - Système de fallback intelligent
- ✅ **Optimisation coûts** : Stratégie hybrid FAQ locale (70%) + IA (30%)
- ✅ **Mode démo** : Développement sans frais avec réponses simulées

### Domaines Couverts

Le bot peut répondre sur :
- 💻 Technologie & Programmation (Java, Spring, Web, IA, BDD)
- 📚 Éducation & Apprentissage
- 💼 Carrière & Développement Professionnel
- 💰 Finance Personnelle
- 🧘 Bien-être & Santé
- 🎯 Développement Personnel

## 🏗️ Architecture
```
┌─────────────────────────────────────────────────────────┐
│                   CLIENT (Angular)                       │
│                   Port 4200                              │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ HTTP REST + WebSocket
                     │
┌────────────────────▼────────────────────────────────────┐
│              SPRING BOOT BACKEND                         │
│                  Port 8080                               │
│                                                          │
│  ┌─────────────────────────────────────────────────┐   │
│  │         CONTROLLERS (API REST)                   │   │
│  │  - MessageController                             │   │
│  │  - ConversationController                        │   │
│  │  - UserController                                │   │
│  │  - WebSocketController                           │   │
│  └──────────────────┬──────────────────────────────┘   │
│                     │                                    │
│  ┌──────────────────▼──────────────────────────────┐   │
│  │         SERVICES (Logique métier)                │   │
│  │  - BotService (IA hybrid)                        │   │
│  │  - OpenAiApiService                              │   │
│  │  - KnowledgeBaseService (FAQ)                    │   │
│  │  - SynonymService (NLP)                          │   │
│  └──────────────────┬──────────────────────────────┘   │
│                     │                                    │
│  ┌──────────────────▼──────────────────────────────┐   │
│  │      REPOSITORIES (Accès données)                │   │
│  │  - UserRepository                                │   │
│  │  - ConversationRepository                        │   │
│  │  - MessageRepository                             │   │
│  └──────────────────┬──────────────────────────────┘   │
│                     │                                    │
└─────────────────────┼────────────────────────────────────┘
                      │
                      │ JPA/Hibernate
                      │
┌─────────────────────▼────────────────────────────────────┐
│            BASE DE DONNÉES PostgreSQL                     │
│                                                           │
│  Tables: users, conversations, messages                   │
└───────────────────────────────────────────────────────────┘
```

## 📦 Installation & Lancement

### Prérequis

- Java 21+
- PostgreSQL 12+
- Maven 4.0+
- (Node.js 18+ pour le frontend - Phase 3)

### Configuration

1. **Cloner le projet**
```bash
git clone https://github.com/MOMEOUATT/bot-intelligent.git
cd bot-intelligent
```

2. **Configurer PostgreSQL**
```sql
CREATE DATABASE bot_intelligent;
```

3. **Configuration backend**

Créer `backend/src/main/resources/application-local.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bot_intelligent
spring.datasource.username=votre_username
spring.datasource.password=votre_password

# OpenAI (optionnel)
openai.api.enabled=true
openai.api.demo-mode=true
openai.api.key=
```

4. **Lancer le backend**
```bash
cd backend
mvn spring-boot:run
```

Backend accessible sur : `http://localhost:8080`

## 🎯 Roadmap

- [x] **Phase 1** : Setup projets, BDD, Git
- [x] **Phase 2** : Backend complet avec API REST, WebSocket, Bot IA
- [ ] **Phase 3** : Frontend Angular avec interface chat
- [ ] **Phase 4** : Fonctionnalités avancées (feedback, profil, recherche)
- [ ] **Phase 5** : Tests & Déploiement

## 📊 Statistiques

- **~3000 lignes** de code backend
- **15+ endpoints** REST
- **30+ FAQ** structurées
- **10+ domaines** de connaissances
- **Mode démo** pour développement gratuit

## 🤝 Contribution

Projet personnel de portfolio. Suggestions bienvenues !

## 📝 Licence

MIT License

## 👨‍💻 Auteur

**Ouattara Maghan Emmanuel-Marie** - [LinkedIn](www.linkedin.com/in/emmanuelmarie-
ouattara)

---

⭐ Si ce projet vous plaît, n'hésitez pas à le star !