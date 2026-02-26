package com.BotIntelligent.backend.service;

import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BotService {

    @Autowired(required = false)
    private OpenAiApiService openAiApiService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SynonymService synonymService;

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    private final Map<String, List<String>> knowledgeBase;
    private final Random random;

    public BotService() {
        this.knowledgeBase = new HashMap<>();
        this.random = new Random();
        initializeKnowledgeBase();
    }

    private void initializeKnowledgeBase() {
        // ========== SALUTATIONS & POLITESSE ==========
        knowledgeBase.put("salutations", Arrays.asList("bonjour", "salut", "hello", "hey", "coucou", "bonsoir", "hi", "yo"));
        knowledgeBase.put("aurevoir", Arrays.asList("au revoir", "bye", "à bientôt", "salut", "ciao", "tchao", "adieu", "à plus"));
        knowledgeBase.put("remerciements", Arrays.asList("merci", "thanks", "merci beaucoup", "super", "génial", "cool", "parfait"));
        knowledgeBase.put("comment_ca_va", Arrays.asList("comment ça va", "ça va", "tu vas bien", "comment tu vas", "quoi de neuf"));

        // ========== BIEN-ÊTRE & SANTÉ ==========
        knowledgeBase.put("sommeil", Arrays.asList("sommeil", "dormir", "insomnie", "fatigue", "repos", "nuit", "réveil", "sieste"));
        knowledgeBase.put("sport", Arrays.asList("sport", "exercice", "fitness", "gym", "musculation", "cardio", "course", "yoga", "marche"));
        knowledgeBase.put("nutrition", Arrays.asList("nutrition", "alimentation", "manger", "nourriture", "régime", "diet", "calories", "repas"));
        knowledgeBase.put("stress", Arrays.asList("stress", "anxiété", "anxieux", "angoisse", "nerveux", "pression", "tendu", "inquiet"));
        knowledgeBase.put("motivation", Arrays.asList("motivation", "motivé", "démotivé", "courage", "objectif", "envie", "but", "ambition"));
        knowledgeBase.put("sante_mentale", Arrays.asList("dépression", "déprimé", "triste", "moral", "psychologie", "mental", "humeur"));
        knowledgeBase.put("meditation", Arrays.asList("méditation", "méditer", "relaxation", "détente", "calme", "zen", "respiration"));

        // ========== TECHNOLOGIE & PROGRAMMATION ==========
        knowledgeBase.put("programmation", Arrays.asList("programmation", "coder", "code", "développement", "développer", "programmer", "script"));
        knowledgeBase.put("langages", Arrays.asList("java", "python", "javascript", "c++", "php", "typescript", "langage", "apprendre coder"));
        knowledgeBase.put("web", Arrays.asList("web", "site web", "html", "css", "frontend", "backend", "full stack", "react", "angular"));
        knowledgeBase.put("intelligence_artificielle", Arrays.asList("ia", "intelligence artificielle", "machine learning", "chatgpt", "ai"));
        knowledgeBase.put("base_donnees", Arrays.asList("base de données", "sql", "mysql", "postgresql", "mongodb", "bdd", "database"));

        // ========== ÉDUCATION & APPRENTISSAGE ==========
        knowledgeBase.put("etudes", Arrays.asList("études", "étudier", "université", "école", "cours", "formation", "diplôme", "master"));
        knowledgeBase.put("examens", Arrays.asList("examen", "test", "contrôle", "partiel", "réviser", "révisions", "bac", "concours"));
        knowledgeBase.put("apprentissage", Arrays.asList("apprendre", "apprentissage", "mémoriser", "comprendre", "retenir", "assimiler"));

        // ========== CARRIÈRE & TRAVAIL ==========
        knowledgeBase.put("carriere", Arrays.asList("carrière", "emploi", "job", "travail", "métier", "profession", "poste", "recrutement"));
        knowledgeBase.put("cv", Arrays.asList("cv", "curriculum", "lettre motivation", "candidature", "portfolio", "linkedin"));
        knowledgeBase.put("entretien", Arrays.asList("entretien", "interview", "recruteur", "embauche", "questions entretien"));
        knowledgeBase.put("productivite", Arrays.asList("productivité", "productif", "concentration", "focus", "organisation", "temps", "pomodoro"));

        // ========== FINANCE & ARGENT ==========
        knowledgeBase.put("budget", Arrays.asList("budget", "argent", "finances", "économiser", "épargne", "dépenses", "gérer argent"));
        knowledgeBase.put("investissement", Arrays.asList("investissement", "investir", "bourse", "actions", "crypto", "trading", "placements"));

        // ========== LOISIRS & CULTURE ==========
        knowledgeBase.put("lecture", Arrays.asList("lecture", "lire", "livre", "roman", "littérature", "recommandation livre"));
        knowledgeBase.put("cinema", Arrays.asList("film", "cinéma", "série", "netflix", "regarder", "movie", "streaming"));
        knowledgeBase.put("musique", Arrays.asList("musique", "chanson", "écouter", "artiste", "concert", "spotify", "playlist"));
        knowledgeBase.put("jeux_video", Arrays.asList("jeu vidéo", "gaming", "jouer", "playstation", "xbox", "console", "gamer"));

        // ========== VOYAGES ==========
        knowledgeBase.put("voyage", Arrays.asList("voyage", "voyager", "destination", "vacances", "tourisme", "pays", "visiter"));

        // ========== RELATIONS & SOCIAL ==========
        knowledgeBase.put("amitie", Arrays.asList("amis", "amitié", "copain", "pote", "relation", "social", "rencontrer"));
        knowledgeBase.put("amour", Arrays.asList("amour", "relation amoureuse", "couple", "rencontre", "dating", "séduire"));

        // ========== AIDE GÉNÉRALE ==========
        knowledgeBase.put("conseil", Arrays.asList("conseil", "aide", "aider", "suggestion", "recommandation", "astuce", "tips"));
    }

    /**
     * Génère une réponse intelligente avec analyse sémantique
     */
    /**
     * Génère une réponse intelligente avec analyse sémantique
     */
    public String generateResponseWithContext(String userMessage, Long conversationId) {
        // 1. Récupérer le contexte de conversation
        String context = getConversationContext(conversationId, 5);

        System.out.println("Message: " + userMessage);

        // 2. Normaliser le message
        String normalizedMessage = synonymService.normalizeText(userMessage);
        System.out.println("Message normalisé: " + normalizedMessage);
        SynonymService.Sentiment sentiment = synonymService.analyzeSentiment(userMessage);

        // 3. Détecter la catégorie
        String category = detectCategory(userMessage);
        System.out.println("category: " + category);

        // 4. D'ABORD : Chercher dans la FAQ locale (GRATUIT et RAPIDE)
        if (category != null) {
            String faqResponse = knowledgeBaseService.searchFAQ(category, normalizedMessage);
            if (faqResponse != null) {
                return faqResponse; // ✅ Réponse FAQ locale (pas de 🤖)
            }
        }

        // 5. Si catégorie connue mais pas de FAQ précise → Réponse par catégorie
        if (category != null && isSimpleCategory(category)) {
            return getCategoryResponse(category, sentiment); // ✅ Réponse locale par catégorie
        }

        // 6. Si pas de catégorie OU catégorie complexe → Utiliser OpenAI
        if (openAiApiService != null && openAiApiService.isAvailable()) {
            String aiResponse = openAiApiService.generateResponse(userMessage, context);
            if (aiResponse != null && !aiResponse.isEmpty()) {
                return aiResponse + " 🤖"; // ✅ Réponse OpenAI (avec indicateur)
            }
        }

        // 7. Fallback : vérifier si c'est une suite de conversation
        String detectedTopic = detectTopicFromContext(context);
        if (detectedTopic != null && isFollowUpMessage(normalizedMessage)) {
            return getContinuationResponse(detectedTopic, normalizedMessage, sentiment);
        }

        // 8. Dernière option : réponse par défaut
        return getDefaultResponse(sentiment);
    }

    /**
     * Détermine si une catégorie peut être traitée localement
     */
    private boolean isSimpleCategory(String category) {
        List<String> simpleCategories = Arrays.asList(
                "salutations", "aurevoir", "remerciements", "comment_ca_va",
                "sommeil", "sport", "nutrition", "stress", "motivation",
                "sante_mentale", "meditation", "conseil"
        );
        return simpleCategories.contains(category);
    }

    /**
     * Détecte la catégorie du message
     */
    private String detectCategory(String message) {
        String messageLower = message.toLowerCase();

        for (Map.Entry<String, List<String>> entry : knowledgeBase.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();

            for (String keyword : keywords) {
                if (messageLower.contains(keyword)) {
                    return category;
                }
            }
        }

        return null;
    }

    /**
     * Génère une réponse adaptée au sentiment
     */
    private String getCategoryResponse(String category, SynonymService.Sentiment sentiment) {
        List<String> responses = new ArrayList<>();

        switch (category) {
            // ========== SALUTATIONS & POLITESSE ==========
            case "salutations":
                if (sentiment == SynonymService.Sentiment.POSITIF) {
                    responses.add("Bonjour ! 😊 Vous semblez en forme ! Comment puis-je vous aider ?");
                } else {
                    responses.add("Bonjour ! 👋 Je suis là pour vous aider. De quoi avez-vous besoin ?");
                }
                break;

            case "aurevoir":
                responses.add("Au revoir ! 👋 À bientôt et bonne journée !");
                responses.add("Bye ! N'hésitez pas à revenir si besoin !");
                break;

            case "remerciements":
                responses.add("Avec plaisir ! 😊 C'est pour ça que je suis là !");
                responses.add("De rien ! N'hésitez pas si vous avez d'autres questions !");
                break;

            case "comment_ca_va":
                responses.add("Je vais très bien, merci ! 😊 Et vous ? Comment puis-je vous aider ?");
                break;

            // ========== BIEN-ÊTRE & SANTÉ ==========
            case "sommeil":
                responses.add("Le sommeil est essentiel ! 😴 Routine fixe, chambre fraîche (18-20°C), pas d'écrans 1h avant.");
                break;

            case "sport":
                if (sentiment == SynonymService.Sentiment.POSITIF) {
                    responses.add("Super motivation ! 💪 L'important est la régularité. Quel type d'activité vous attire ?");
                } else {
                    responses.add("Je comprends que ce soit difficile. 🚶 10-15 min de marche pour commencer ?");
                }
                break;

            case "nutrition":
                responses.add("Alimentation équilibrée : 1/2 légumes, 1/4 protéines, 1/4 féculents + bonnes graisses. 🥗");
                break;

            case "stress":
                if (sentiment == SynonymService.Sentiment.NEGATIF) {
                    responses.add("Je sens que vous êtes stressé. 🫂 Respirez profondément : 4-7-8 (inspiration, pause, expiration).");
                } else {
                    responses.add("Gérer son stress : respiration profonde, exercice, méditation. 🧘");
                }
                break;

            case "motivation":
                if (sentiment == SynonymService.Sentiment.NEGATIF) {
                    responses.add("Le manque de motivation arrive à tous. 🌟 " + knowledgeBaseService.getRandomQuote());
                } else {
                    responses.add("Excellent état d'esprit ! 🚀 " + knowledgeBaseService.getRandomQuote());
                }
                break;

            case "sante_mentale":
                responses.add("Votre santé mentale compte autant que votre santé physique. 💚 N'hésitez pas à consulter un professionnel.");
                break;

            case "meditation":
                responses.add("La méditation a des bienfaits prouvés ! 🧘 Commencez par 5 min/jour, concentrez-vous sur votre respiration.");
                break;

            // ========== TECHNOLOGIE & PROGRAMMATION ==========
            case "programmation":
                responses.add("La programmation, c'est passionnant ! 💻 Quel langage vous intéresse ? Python, Java, JavaScript ?");
                responses.add("Coder demande de la pratique ! Projets concrets, lire du code, participer à l'open source. 🚀");
                break;

            case "langages":
                responses.add("Chaque langage a ses forces ! Python = data/IA, Java = entreprise, JavaScript = web. Votre projet ?");
                break;

            case "web":
                responses.add("Développement web : Frontend (HTML/CSS/JS) + Backend (Java/Python/Node) + BDD. 🌐");
                break;

            case "intelligence_artificielle":
                responses.add("L'IA transforme le monde ! 🤖 Machine Learning, Deep Learning... Python + TensorFlow/PyTorch.");
                break;

            case "base_donnees":
                responses.add("Bases de données : SQL (PostgreSQL, MySQL) vs NoSQL (MongoDB). Selon vos besoins ! 💾");
                break;

            // ========== ÉDUCATION & APPRENTISSAGE ==========
            case "etudes":
                responses.add("Les études demandent organisation ! 📚 Planning, révisions régulières, pauses, sommeil.");
                break;

            case "examens":
                responses.add("Préparer un examen : planifiez, révisez activement, testez-vous, dormez bien la veille ! 📝");
                break;

            case "apprentissage":
                responses.add("Bien apprendre : technique Feynman (expliquer simplement), répétition espacée, tests actifs. 🧠");
                break;

            // ========== CARRIÈRE & TRAVAIL ==========
            case "carriere":
                responses.add("Construire sa carrière : compétences techniques + soft skills, réseau, projets perso. 🚀");
                break;

            case "cv":
                responses.add("CV efficace : concis (1-2 pages), résultats quantifiés, mots-clés du poste, projets concrets ! 📄");
                break;

            case "entretien":
                responses.add("Entretien : préparez exemples concrets, recherchez l'entreprise, questions à poser, soyez vous-même ! 🎯");
                break;

            case "productivite":
                responses.add("Productivité : Pomodoro (25 min + 5 min pause), éliminez distractions, une tâche à la fois ! ⏰");
                break;

            // ========== FINANCE & ARGENT ==========
            case "budget":
                responses.add("Gérer son budget : suivez vos dépenses, 50% besoins / 30% envies / 20% épargne. 💰");
                break;

            case "investissement":
                responses.add("Investissement : diversification, horizon long terme, comprenez ce que vous achetez ! 📈");
                break;

            // ========== LOISIRS & CULTURE ==========
            case "lecture":
                responses.add("La lecture enrichit ! 📚 Variez les genres, 15 min/jour, rejoignez un club de lecture ?");
                break;

            case "cinema":
                responses.add("Films et séries : excellents pour se détendre ! 🎬 Quel genre vous attire ?");
                break;

            case "musique":
                responses.add("La musique booste l'humeur ! 🎵 Quel style écoutez-vous ?");
                break;

            case "jeux_video":
                responses.add("Les jeux vidéo, c'est fun ! 🎮 Quel type de jeu vous plaît ?");
                break;

            // ========== VOYAGES ==========
            case "voyage":
                responses.add("Voyager ouvre l'esprit ! ✈️ Où voulez-vous aller ?");
                break;

            // ========== RELATIONS & SOCIAL ==========
            case "amitie":
                responses.add("Les amis sont précieux ! 👥 Entretenez vos relations, soyez à l'écoute.");
                break;

            case "amour":
                responses.add("L'amour demande communication et respect ! 💕 Soyez vous-même, patience.");
                break;

            // ========== AIDE GÉNÉRALE ==========
            case "conseil":
                responses.add("Je suis là pour vous conseiller ! 💡 Sur quoi avez-vous besoin d'aide ?");
                break;

            default:
                return getDefaultResponse(sentiment);
        }

        if (responses.isEmpty()) {
            return getDefaultResponse(sentiment);
        }

        return responses.get(random.nextInt(responses.size()));
    }

    private String getDefaultResponse(SynonymService.Sentiment sentiment) {
        if (sentiment == SynonymService.Sentiment.NEGATIF) {
            return "Je sens que quelque chose vous préoccupe. 💙 Je peux vous aider sur plein de sujets : tech, études, carrière, bien-être... Qu'est-ce qui vous tracasse ?";
        } else if (sentiment == SynonymService.Sentiment.POSITIF) {
            return "Content de discuter avec vous ! 😊 Je peux vous aider sur la tech, les études, le travail, le bien-être... Que souhaitez-vous savoir ?";
        } else {
            return "Je peux vous conseiller sur la programmation, les études, la carrière, le bien-être, les finances... De quoi souhaitez-vous parler ? 🤔";
        }
    }

    // Méthodes existantes (gardez-les)
    private String detectTopicFromContext(String context) {
        if (context == null || context.isEmpty()) return null;

        String contextLower = context.toLowerCase();
        Map<String, Integer> topicScores = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : knowledgeBase.entrySet()) {
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (contextLower.contains(keyword)) score++;
            }
            if (score > 0) topicScores.put(entry.getKey(), score);
        }

        return topicScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private boolean isFollowUpMessage(String message) {
        List<String> followUpPhrases = Arrays.asList(
                "ça marche pas", "pas vraiment", "autre chose", "et si", "mais",
                "plutôt", "sinon", "encore", "plus", "non", "oui mais"
        );

        for (String phrase : followUpPhrases) {
            if (message.contains(phrase)) return true;
        }
        return false;
    }

    private String getContinuationResponse(String topic, String message, SynonymService.Sentiment sentiment) {
        switch (topic) {
            case "stress":
                return "Je comprends. 💙 Peut-être essayer des techniques différentes ? Méditation guidée (apps : Petit Bambou, Calm), ou simplement parler à quelqu'un ?";
            case "sommeil":
                return "Les problèmes de sommeil sont tenaces. 😴 Avez-vous essayé de tenir un journal du sommeil ? Noter l'heure du coucher/réveil peut révéler des patterns.";
            case "sport":
                return "Peut-être qu'un autre type d'activité vous conviendrait ? Yoga, natation, ou même juste de la marche. L'important : trouver ce que vous aimez ! 🚶";
            default:
                return "Je vois. Voulez-vous qu'on explore une autre piste ou préférez-vous parler d'autre chose ? 😊";
        }
    }

    private String getConversationContext(Long conversationId, int numberOfMessages) {
        if (conversationId == null) return "";

        List<Message> recentMessages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        if (recentMessages.isEmpty()) return "";

        int startIndex = Math.max(0, recentMessages.size() - numberOfMessages);
        List<Message> contextMessages = recentMessages.subList(startIndex, recentMessages.size());

        StringBuilder context = new StringBuilder();
        for (Message msg : contextMessages) {
            String role = msg.getIsBot() ? "Bot" : "User";
            context.append(role).append(": ").append(msg.getContent()).append("\n");
        }

        return context.toString();
    }
}