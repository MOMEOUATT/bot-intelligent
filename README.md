# 🤖 Bot Intelligent - Chatbot Full-Stack avec IA

Application web de chatbot intelligent polyvalent intégrant l'API OpenAI pour des réponses contextuelles et naturelles.

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green)
![Angular](https://img.shields.io/badge/Angular-20-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

## 🚀 Technologies

### Backend
- **Java 21** & **Spring Boot 4**
- **Spring Data JPA** / Hibernate
- **PostgreSQL**
- **WebSocket** / STOMP
- **OpenAI API** (GPT-4o-mini)
- **Maven**

### Frontend
- **Angular 20** (standalone components)
- **TypeScript 5.8**
- **Angular Material 20**
- **RxJS** (programmation réactive)
- **Chart.js 4.4** + **ng2-charts**
- **SCSS** avec variables CSS

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

### Frontend (Phase 3 - ✅ Terminée)

- ✅ **Interface chat moderne** avec suggestions prédéfinies
- ✅ **Sidebar conversations** avec recherche et filtrage
- ✅ **Messages en temps réel** via WebSocket
- ✅ **Feedback utilisateur** (like/dislike/copy)
- ✅ **Authentification complète** (login/register/guards)
- ✅ **Gestion profil** (édition username/email, changement password)

### Dashboard & Analytics (Phase 4 - ✅ Terminée)

- ✅ **4 KPIs** : conversations, messages, réponses utiles, à améliorer
- ✅ **Graphique messages/jour** avec périodes 7/14/30/90 jours
- ✅ **Graphique répartition feedback** (donut avec pourcentages)
- ✅ **Conversations récentes** avec navigation rapide
- ✅ **Design responsive** adaptatif mobile/desktop

### Bonus Features (✅ Terminées)

- ✅ **Thème clair/sombre** avec toggle et persistance localStorage
- ✅ **Notifications toast** (4 types : success/error/info/warning)
- ✅ **Renommage conversations** avec modal élégant
- ✅ **Design moderne** Material Design 3 avec animations
- ✅ **Sidebar collapsible** pour optimiser l'espace


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

## 📁 Structure du Projet
```
bot-intelligent-projet/
├── backend/                    # Spring Boot (✅ Terminé)
│   ├── controllers/           # REST + WebSocket
│   ├── entities/              # JPA Models
│   ├── repositories/          # Data Access
│   ├── service/               # Business Logic
│   └── config/                # Configuration
│
├── frontend/                   # Angular (✅ Terminé)
│   ├── components/
│   │   ├── auth/              # Login/Register
│   │   ├── chat/              # Interface principale
│   │   ├── sidebar/           # Liste conversations
│   │   ├── header/            # Navigation
│   │   ├── dashboard/         # Analytics + widgets
│   │   └── user-profile/      # Gestion profil
│   ├── services/              # API, Auth, WebSocket, Theme
│   ├── models/                # TypeScript interfaces
│   └── guards/                # Route protection
│
└── README.md
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

5. **Configuration frontend**
```bash
cd frontend

# Installer les dépendances
npm install
```

6. **Lancer le frontend**
```bash
npm start
```

Frontend accessible sur : `http://localhost:4200`

## 🎯 Roadmap

- [x] **Phase 1** : Setup projets, BDD, Git ✅
- [x] **Phase 2** : Backend complet avec API REST, WebSocket, Bot IA ✅
- [x] **Phase 3** : Frontend Angular avec interface chat ✅
- [x] **Phase 4** : Dashboard, Analytics, Graphiques ✅
- [x] **Bonus** : Thème clair/sombre, Notifications, Profil ✅
- [ ] **Phase 5** : Tests complets & Déploiement 🚧

### 🔮 Futures Fonctionnalités (v2.0)

- [ ] Upload de fichiers (préparé mais désactivé)
- [ ] **Extension IA avancée** (préparée, non intégrée)
  - RAG-powered responses (ChromaDB)
  - Sentiment analysis (NLP Python)
  - Advanced analytics (FastAPI microservice)
- [ ] Multi-langues (FR/EN)
- [ ] Application mobile (Ionic)
- [ ] Export conversations (PDF/TXT)
- [ ] Mode hors ligne (PWA)

## 📊 Statistiques du Projet

- **~8000 lignes** de code total (Backend 4K + Frontend 4K)
- **20+ composants** Angular standalone
- **15+ endpoints** REST + WebSocket
- **4 modèles** JPA (User, Conversation, Message, Stats)
- **30+ FAQ** base de connaissances
- **10+ domaines** de connaissances couverts
- **2 thèmes** (sombre + clair grisé)
- **Mode démo** pour développement sans coûts

## 📖 Utilisation Rapide

1. **Créer un compte** : S'inscrire avec username, email, password
2. **Nouvelle conversation** : Bouton "+" ou cliquer une suggestion
3. **Donner feedback** : 👍 Utile, 👎 Pas utile, 📋 Copier
4. **Dashboard** : Menu (⋮) → Tableau de bord
5. **Changer thème** : Icône ☀️/🌙 dans le header
6. **Recherche** : Barre de recherche dans sidebar

## 🤝 Contribution

Projet personnel de portfolio. Suggestions bienvenues !

## 📝 Licence

MIT License

## 👨‍💻 Auteur

**Ouattara Maghan Emmanuel-Marie** 
- 🔗 LinkedIn : [emmanuelmarie-ouattara](https://www.linkedin.com/in/emmanuelmarie-ouattara)
- 📧 Email : emmanuelmarieouattara@gmail.com
- 🐙 GitHub : [@MOMEOUATT](https://github.com/MOMEOUATT)
- 🔗 Portfolio: [Emmanuel-Marie_Ouattara/Portfolio](https://portfolio-ouattara.vercel.app/)

---

⭐ Si ce projet vous plaît, n'hésitez pas à le star !
