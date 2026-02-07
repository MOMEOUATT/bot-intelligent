package com.BotIntelligent.backend.service;

import com.BotIntelligent.backend.entities.Message;
import com.BotIntelligent.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BotService {

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
        // Mots-clés principaux (gardez ceux existants)
        knowledgeBase.put("salutations", Arrays.asList("bonjour", "salut", "hello", "hey", "coucou", "bonsoir", "hi"));
        knowledgeBase.put("aurevoir", Arrays.asList("au revoir", "bye", "à bientôt", "salut", "ciao", "tchao"));
        knowledgeBase.put("remerciements", Arrays.asList("merci", "thanks", "merci beaucoup", "super", "génial"));
        knowledgeBase.put("comment_ca_va", Arrays.asList("comment ça va", "ça va", "tu vas bien", "quoi de neuf"));
        knowledgeBase.put("sommeil", Arrays.asList("sommeil", "dormir", "insomnie", "fatigue", "repos", "nuit"));
        knowledgeBase.put("sport", Arrays.asList("sport", "exercice", "fitness", "gym", "musculation", "cardio"));
        knowledgeBase.put("nutrition", Arrays.asList("nutrition", "alimentation", "manger", "nourriture", "régime"));
        knowledgeBase.put("stress", Arrays.asList("stress", "anxiété", "anxieux", "angoisse", "nerveux"));
        knowledgeBase.put("motivation", Arrays.asList("motivation", "motivé", "démotivé", "courage", "objectif"));
        knowledgeBase.put("sante_mentale", Arrays.asList("dépression", "déprimé", "triste", "moral", "psychologie"));
        knowledgeBase.put("productivite", Arrays.asList("productivité", "productif", "travail", "concentration"));
        knowledgeBase.put("meditation", Arrays.asList("méditation", "méditer", "relaxation", "détente", "calme"));
    }

    /**
     * Génère une réponse intelligente avec analyse sémantique
     */
    public String generateResponseWithContext(String userMessage, Long conversationId) {
        // 1. Normaliser le message (remplacer synonymes)
        String normalizedMessage = synonymService.normalizeText(userMessage);

        // 2. Analyser le sentiment
        SynonymService.Sentiment sentiment = synonymService.analyzeSentiment(userMessage);

        // 3. Récupérer le contexte
        String context = getConversationContext(conversationId, 5);
        String detectedTopic = detectTopicFromContext(context);

        // 4. Vérifier si c'est une suite de conversation
        if (detectedTopic != null && isFollowUpMessage(normalizedMessage)) {
            return getContinuationResponse(detectedTopic, normalizedMessage, sentiment);
        }

        // 5. Chercher dans la FAQ enrichie d'abord
        String category = detectCategory(normalizedMessage);
        if (category != null) {
            String faqResponse = knowledgeBaseService.searchFAQ(category, normalizedMessage);
            if (faqResponse != null) {
                return faqResponse;
            }
        }

        // 6. Réponse basée sur la catégorie détectée
        if (category != null) {
            return getCategoryResponse(category, sentiment);
        }

        // 7. Réponse par défaut adaptée au sentiment
        return getDefaultResponse(sentiment);
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
            case "salutations":
                if (sentiment == SynonymService.Sentiment.POSITIF) {
                    responses.add("Bonjour ! 😊 Vous semblez en forme ! Comment puis-je vous aider ?");
                } else {
                    responses.add("Bonjour ! 👋 Je suis là pour vous écouter. Comment allez-vous ?");
                }
                break;

            case "stress":
                if (sentiment == SynonymService.Sentiment.NEGATIF) {
                    responses.add("Je sens que vous êtes stressé. 🫂 Prenez une grande respiration. Voulez-vous qu'on parle de techniques de relaxation ?");
                    responses.add("Le stress peut être écrasant. 💙 N'hésitez pas à en parler à quelqu'un de confiance ou à consulter un professionnel si ça devient trop lourd.");
                } else {
                    responses.add("Gérer son stress, c'est important ! 🧘 La respiration profonde, l'exercice et la méditation sont très efficaces.");
                }
                break;

            case "sommeil":
                responses.add("Le sommeil est la base ! 😴 Routine fixe, chambre fraîche (18-20°C), pas d'écrans 1h avant. Combien d'heures dormez-vous en moyenne ?");
                break;

            case "sport":
                if (sentiment == SynonymService.Sentiment.POSITIF) {
                    responses.add("Super motivation ! 💪 L'important est la régularité. Quel type d'activité vous attire ?");
                } else {
                    responses.add("Je comprends que ce soit difficile de commencer. 🚶 Que diriez-vous de simplement 10 minutes de marche par jour pour débuter ?");
                }
                break;

            case "motivation":
                if (sentiment == SynonymService.Sentiment.NEGATIF) {
                    responses.add("Le manque de motivation arrive à tout le monde. 🌟 " + knowledgeBaseService.getRandomQuote());
                } else {
                    responses.add("Excellent état d'esprit ! 🚀 " + knowledgeBaseService.getRandomQuote());
                }
                break;

            case "sante_mentale":
                responses.add("Votre santé mentale est aussi importante que votre santé physique. 💚 N'hésitez jamais à consulter un professionnel si vous en ressentez le besoin.");
                break;

            default:
                responses.add("Intéressant ! Pouvez-vous m'en dire un peu plus pour que je puisse mieux vous aider ?");
        }

        if (responses.isEmpty()) {
            return getDefaultResponse(sentiment);
        }

        return responses.get(random.nextInt(responses.size()));
    }

    private String getDefaultResponse(SynonymService.Sentiment sentiment) {
        if (sentiment == SynonymService.Sentiment.NEGATIF) {
            return "Je sens que quelque chose vous préoccupe. 💙 Je suis là pour parler de sommeil, sport, nutrition, stress, motivation... Qu'est-ce qui vous tracasse ?";
        } else if (sentiment == SynonymService.Sentiment.POSITIF) {
            return "Content de discuter avec vous ! 😊 Je peux vous aider sur le sommeil, sport, nutrition, bien-être... Que souhaitez-vous savoir ?";
        } else {
            return "Je peux vous conseiller sur le sommeil, sport, nutrition, stress, motivation... De quoi souhaitez-vous parler ? 🤔";
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