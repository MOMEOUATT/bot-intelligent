package com.BotIntelligent.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class OpenAiApiService {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.enabled:false}")
    private boolean apiEnabled;

    @Value("${openai.api.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.api.demo-mode:false}")
    private boolean demoMode;

    private final RestTemplate restTemplate;

    public OpenAiApiService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Génère une réponse intelligente avec OpenAI API
     */
    public String generateResponse(String userMessage, String conversationContext) {
        if (!apiEnabled || apiKey == null || apiKey.isEmpty()) {
            return null; // Fallback vers bot local
        }

        // MODE DÉMO : Réponses simulées sans coût
        if (demoMode) {
            return generateDemoResponse(userMessage);
        }

        try {
            return callOpenAI(userMessage, conversationContext);
        } catch (Exception e) {
            System.err.println("Erreur API OpenAI: " + e.getMessage());
            e.printStackTrace();
            return null; // Fallback
        }
    }

    private String callOpenAI(String userMessage, String conversationContext) {
        String url = "https://api.openai.com/v1/chat/completions";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Construction des messages
        List<Map<String, String>> messages = buildMessages(userMessage, conversationContext);

        // Body de la requête
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Appel API
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        // Extraction de la réponse
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                return message.get("content");
            }
        }

        return null;
    }

    private List<Map<String, String>> buildMessages(String userMessage, String conversationContext) {
        List<Map<String, String>> messages = new ArrayList<>();

        // 1. Message système (personnalité du bot)
        messages.add(Map.of(
                "role", "system",
                "content", buildSystemPrompt()
        ));

        // 2. Historique de conversation (si disponible)
        if (conversationContext != null && !conversationContext.isEmpty()) {
            messages.add(Map.of(
                    "role", "assistant",
                    "content", "Contexte de conversation récent : " + conversationContext
            ));
        }

        // 3. Message actuel de l'utilisateur
        messages.add(Map.of(
                "role", "user",
                "content", userMessage
        ));

        return messages;
    }

    private String buildSystemPrompt() {
        return "Tu es un assistant personnel intelligent, bienveillant et polyvalent nommé 'Bot Intelligent'. "
                + "Tu aides les utilisateurs sur des sujets variés : technologie, programmation, "
                + "études, carrière, développement personnel, bien-être, finances, loisirs, etc.\n\n"

                + "PERSONNALITÉ :\n"
                + "- Ton empathique, chaleureux et encourageant\n"
                + "- Adapte ton niveau de langage à l'utilisateur\n"
                + "- Donne des conseils pratiques et actionnables\n"
                + "- Utilise des exemples concrets\n"
                + "- Pose des questions de clarification si besoin\n\n"

                + "EXPERTISE :\n"
                + "- Technologie : programmation (Python, Java, JavaScript, etc.), web dev, IA, bases de données\n"
                + "- Éducation : techniques d'apprentissage, révisions, examens\n"
                + "- Carrière : CV, entretiens, développement professionnel, productivité\n"
                + "- Finance : budget, épargne, investissements (niveau débutant)\n"
                + "- Bien-être : sommeil, sport, nutrition, gestion du stress\n"
                + "- Développement personnel : motivation, objectifs, habitudes\n\n"

                + "FORMAT DES RÉPONSES :\n"
                + "- Réponses concises mais complètes (2-5 phrases ou une liste courte)\n"
                + "- Structure claire : explication + conseil pratique + question d'engagement\n"
                + "- Utilise des emojis avec parcimonie (1-2 par message maximum)\n"
                + "- Pour code : fournis des exemples courts et commentés\n\n"

                + "SÉCURITÉ :\n"
                + "- Questions médicales sérieuses : recommande de consulter un professionnel\n"
                + "- Détresse psychologique : encourage à contacter un psychologue ou ligne d'écoute\n"
                + "- Conseils financiers complexes : précise que tu n'es pas conseiller financier\n\n"

                + "STYLE :\n"
                + "- Naturel et conversationnel (pas trop formel)\n"
                + "- Encourageant sans être condescendant\n"
                + "- Honnête sur tes limites\n"
                + "- Tiens compte du contexte de la conversation";
    }

    /**
     * Vérifie si l'API est configurée et disponible
     */
    public boolean isAvailable() {
        return apiEnabled && apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Retourne le modèle utilisé
     */
    public String getModel() {
        return model;
    }

    /**
     * Génère des réponses simulées intelligentes pour le mode démo
     */
    private String generateDemoResponse(String userMessage) {
        String messageLower = userMessage.toLowerCase();

        // ========== PROGRAMMATION JAVA ==========
        if (messageLower.contains("arraylist") || messageLower.contains("linkedlist")) {
            return "La différence principale réside dans leur structure interne :\n\n"
                    + "**ArrayList** utilise un tableau dynamique. L'accès par index est très rapide (O(1)), "
                    + "mais l'insertion/suppression au milieu est lente (O(n)) car il faut décaler les éléments.\n\n"
                    + "**LinkedList** utilise une liste doublement chaînée. L'insertion/suppression sont rapides (O(1)) "
                    + "si on a déjà la référence, mais l'accès par index est lent (O(n)).\n\n"
                    + "**Utilisez ArrayList** pour accès fréquent par index et peu de modifications. "
                    + "**Utilisez LinkedList** pour nombreuses insertions/suppressions.\n\n"
                    + "Dans 95% des cas, **ArrayList est préférable** ! Vous avez un cas d'usage spécifique ?";
        }

        if (messageLower.contains("java") && (messageLower.contains("stream") || messageLower.contains("lambda"))) {
            return "Les Streams Java (depuis Java 8) permettent le traitement fonctionnel des collections :\n\n"
                    + "```java\n"
                    + "list.stream()\n"
                    + "    .filter(x -> x > 10)\n"
                    + "    .map(x -> x * 2)\n"
                    + "    .collect(Collectors.toList());\n"
                    + "```\n\n"
                    + "**Avantages** : code plus lisible, lazy evaluation, parallélisable (.parallelStream()). "
                    + "**Opérations courantes** : filter, map, reduce, collect, forEach. "
                    + "Parfait pour transformer des collections de manière déclarative ! ";
        }

        // ========== SPRING BOOT ==========
        if (messageLower.contains("spring") || messageLower.contains("boot")) {
            return "Spring Boot simplifie énormément le développement Java ! "
                    + "**Annotations essentielles** :\n"
                    + "- @SpringBootApplication : point d'entrée\n"
                    + "- @RestController : API REST\n"
                    + "- @Service : logique métier\n"
                    + "- @Repository : accès données\n"
                    + "- @Autowired : injection de dépendances\n\n"
                    + "L'auto-configuration et le serveur Tomcat embarqué permettent de démarrer en minutes. "
                    + "Excellent choix pour des applications robustes et scalables ! ";
        }

        if (messageLower.contains("jpa") || messageLower.contains("hibernate")) {
            return "JPA (Java Persistence API) avec Hibernate facilite l'accès aux données :\n\n"
                    + "**Annotations clés** : @Entity, @Id, @GeneratedValue, @OneToMany, @ManyToOne\n"
                    + "**Repositories** : interface JpaRepository pour CRUD automatique\n"
                    + "**Requêtes** : méthodes dérivées (findByEmail), @Query pour JPQL/SQL natif\n\n"
                    + "Avantage : moins de SQL manuel, mapping objet-relationnel automatique. "
                    + "Attention aux requêtes N+1, utilisez fetch joins quand nécessaire ! ";
        }

        // ========== WEB & API ==========
        if (messageLower.contains("rest") || messageLower.contains("api")) {
            return "Une API REST bien conçue suit ces principes :\n\n"
                    + "1. **Ressources** identifiées par URLs (/users, /users/123)\n"
                    + "2. **Méthodes HTTP** standards (GET, POST, PUT, DELETE)\n"
                    + "3. **Codes status** appropriés (200, 201, 404, 500)\n"
                    + "4. **Format JSON** pour requêtes/réponses\n"
                    + "5. **Versioning** (v1, v2) pour évolutions\n\n"
                    + "Pensez aussi : authentification (JWT), rate limiting, validation inputs, documentation (Swagger). "
                    + "Une bonne API est prévisible et bien documentée ! ";
        }

        if (messageLower.contains("websocket") || messageLower.contains("temps réel")) {
            return "WebSocket permet la communication bidirectionnelle en temps réel :\n\n"
                    + "**Vs HTTP classique** : connexion persistante vs requête/réponse\n"
                    + "**Use cases** : chat, notifications live, dashboards temps réel, jeux multijoueurs\n"
                    + "**Avec Spring** : @EnableWebSocketMessageBroker, STOMP protocol\n\n"
                    + "Architecture : client se connecte une fois, serveur peut pousser des messages instantanément. "
                    + "Parfait pour votre chatbot ! ";
        }

        // ========== BASE DE DONNÉES ==========
        if (messageLower.contains("postgresql") || messageLower.contains("mysql") ||
                messageLower.contains("base de données") || messageLower.contains("sql")) {
            return "**Choix base de données relationnelle** :\n\n"
                    + "**PostgreSQL** : excellent pour données complexes, transactions ACID, JSON support, performances. Choix polyvalent recommandé !\n"
                    + "**MySQL** : simplicité, rapidité, large adoption. Bon pour applications classiques.\n"
                    + "**MongoDB** (NoSQL) : flexibilité schéma, scalabilité horizontale facile.\n\n"
                    + "**Critères décision** : structure données (fixe vs flexible), volume, besoins transactionnels, équipe. "
                    + "Pour débuter : PostgreSQL est excellent ! ";
        }

        // ========== CARRIÈRE & CV ==========
        if (messageLower.contains("cv") || messageLower.contains("curriculum")) {
            return "**CV développeur impactant** :\n\n"
                    + "1. **Projets GitHub** mis en avant avec descriptions claires\n"
                    + "2. **Résultats quantifiés** ('Réduit temps réponse de 40%')\n"
                    + "3. **Stack technique** alignée avec le poste visé\n"
                    + "4. **Format** : 1-2 pages max, PDF, zéro faute\n"
                    + "5. **Sections** : Compétences, Projets, Expérience, Formation\n\n"
                    + "Incluez contributions open-source, certifications. "
                    + "Le portfolio GitHub peut valoir plus qu'un diplôme ! ";
        }

        if (messageLower.contains("entretien") || messageLower.contains("interview")) {
            return "**Préparation entretien développeur** :\n\n"
                    + "1. **Méthode STAR** pour vos exemples (Situation, Tâche, Action, Résultat)\n"
                    + "2. **Révisez fondamentaux** : structures données, complexité algorithmique\n"
                    + "3. **Préparez questions techniques** probables selon stack du poste\n"
                    + "4. **Questions à poser** : équipe, méthodologies, stack, roadmap produit\n"
                    + "5. **Expliquez vos choix** techniques dans vos projets\n\n"
                    + "Confiance et authenticité sont clés. Admettez ce que vous ne savez pas ! ";
        }

        // ========== GIT & VERSIONNING ==========
        if (messageLower.contains("git") || messageLower.contains("github")) {
            return "**Bonnes pratiques Git** :\n\n"
                    + "1. **Commits atomiques** avec messages clairs ('Fix: login bug' vs 'update')\n"
                    + "2. **Branches** : feature/nom pour chaque fonctionnalité\n"
                    + "3. **Pull requests** avec code review\n"
                    + "4. **Gitflow** : master/develop/feature pour projets collaboratifs\n"
                    + "5. **.gitignore** : exclure fichiers sensibles/build\n\n"
                    + "**Commandes essentielles** : add, commit, push, pull, branch, merge, rebase. "
                    + "GitHub est votre portfolio visible ! ";
        }

        // ========== FRONTEND ==========
        if (messageLower.contains("angular") || messageLower.contains("react") ||
                messageLower.contains("vue") || messageLower.contains("frontend")) {
            return "**Frameworks frontend modernes** :\n\n"
                    + "**React** : flexibilité, écosystème riche, JSX, hooks\n"
                    + "**Angular** : structure complète, TypeScript natif, enterprise-ready\n"
                    + "**Vue** : courbe apprentissage douce, progressive\n\n"
                    + "**Choix selon** : taille projet, équipe, préférences. "
                    + "TypeScript recommandé pour tous (typage = moins bugs). "
                    + "Maîtrisez : composants, state management, lifecycle. Lequel vous intéresse ? ";
        }

        // ========== SÉCURITÉ ==========
        if (messageLower.contains("sécurité") || messageLower.contains("jwt") ||
                messageLower.contains("authentification")) {
            return "**Sécurité application web** :\n\n"
                    + "1. **Authentification** : JWT ou sessions sécurisées\n"
                    + "2. **HTTPS** obligatoire en production\n"
                    + "3. **Validation inputs** : protection XSS, injection SQL\n"
                    + "4. **CORS** configuré correctement\n"
                    + "5. **Secrets** en variables environnement (jamais dans code !)\n"
                    + "6. **Rate limiting** contre attaques brute force\n\n"
                    + "Spring Security simplifie beaucoup. La sécurité n'est jamais optionnelle ! ";
        }

        // ========== TESTS ==========
        if (messageLower.contains("test") || messageLower.contains("junit")) {
            return "**Tests pour code robuste** :\n\n"
                    + "**Tests unitaires** (JUnit, Mockito) : fonctions isolées\n"
                    + "**Tests d'intégration** : interactions composants\n"
                    + "**Tests E2E** : parcours utilisateur complet\n\n"
                    + "Visez 70-80% couverture. **TDD** (Test-Driven Development) : écrire test avant code. "
                    + "Avantages : trouve bugs tôt, facilite refactoring, documente comportement attendu. "
                    + "Bon investissement temps ! ";
        }

        // ========== DOCKER & DÉPLOIEMENT ==========
        if (messageLower.contains("docker") || messageLower.contains("déploiement") ||
                messageLower.contains("container")) {
            return "**Docker conteneurise vos applications** :\n\n"
                    + "**Dockerfile** : définit l'environnement (FROM, RUN, COPY, CMD)\n"
                    + "**docker-compose** : orchestration multi-containers (app + DB + cache)\n\n"
                    + "**Avantages** : même environnement dev/prod, isolation, portabilité, scalabilité\n"
                    + "**Déploiement** : AWS, GCP, Azure supportent tous Docker. Kubernetes pour orchestration large échelle.\n\n"
                    + "Pour débuter : Dockerfile simple, puis compose.yml pour stack complète ! ";
        }

        // ========== ALGORITHMES ==========
        if (messageLower.contains("algorithme") || messageLower.contains("complexité") ||
                messageLower.contains("o(n)") || messageLower.contains("big o")) {
            return "**Complexité algorithmique** (Big O) :\n\n"
                    + "**O(1)** - Constant : accès HashMap, get ArrayList par index\n"
                    + "**O(log n)** - Logarithmique : recherche binaire\n"
                    + "**O(n)** - Linéaire : parcours simple de tableau\n"
                    + "**O(n log n)** - Linéarithmique : tri efficace (mergesort)\n"
                    + "**O(n²)** - Quadratique : boucles imbriquées (éviter !)\n\n"
                    + "Choisissez structures données appropriées : HashMap pour lookup rapide, TreeSet pour ordre trié. "
                    + "Optimisez les boucles imbriquées en priorité ! ";
        }

        // ========== DÉVELOPPEMENT PERSONNEL ==========
        if (messageLower.contains("apprendre") || messageLower.contains("débutant") ||
                messageLower.contains("commencer")) {
            return "**Apprendre la programmation efficacement** :\n\n"
                    + "1. **Projets concrets** : apprenez en construisant (to-do app, blog, API)\n"
                    + "2. **Pratiquez quotidiennement** : même 30 min/jour > 5h le weekend\n"
                    + "3. **Lisez du code** : GitHub, open-source, analysez comment c'est fait\n"
                    + "4. **Debuggez** : comprendre ses erreurs = meilleur apprentissage\n"
                    + "5. **Communauté** : Stack Overflow, Discord dev, meetups\n\n"
                    + "Patience et persévérance ! Tout dev expert était débutant un jour. ";
        }

        // ========== RÉPONSE GÉNÉRIQUE INTELLIGENTE ==========
        return "Excellente question ! Pour vous donner la réponse la plus pertinente et adaptée à votre situation, "
                + "pourriez-vous préciser un peu plus votre contexte ? Par exemple :\n\n"
                + "- S'agit-il d'un projet personnel ou professionnel ?\n"
                + "- Quel est votre niveau actuel sur ce sujet ?\n"
                + "- Y a-t-il des contraintes spécifiques (délais, technologies imposées) ?\n\n"
                + "Cela m'aidera à vous orienter au mieux ! ";
    }
}