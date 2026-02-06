package com.BotIntelligent.backend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotService {

    private final Map<String, String> knowledgeBase = new HashMap<>();

    public BotService(){
        initializeKnowledgeBase();
    }

    private void initializeKnowledgeBase(){
        // Salutations
        knowledgeBase.put("bonjour", "Bonjour ! Je suis votre assistant intelligent. Comment puis-je vous aider aujourd'hui ?");
        knowledgeBase.put("salut", "Salut ! Ravi de vous revoir. Que puis-je faire pour vous ?");
        knowledgeBase.put("hello", "Hello ! Comment puis-je vous assister ?");
        knowledgeBase.put("bonsoir", "Bonsoir ! Comment allez-vous ?");

        // Sommeil
        knowledgeBase.put("sommeil", "Pour améliorer votre sommeil, voici quelques conseils :\n" +
                "- Couchez-vous à heures régulières\n" +
                "- Évitez les écrans 1h avant de dormir\n" +
                "- Créez un environnement calme et sombre\n" +
                "- Évitez la caféine après 16h");

        knowledgeBase.put("dormir", "Avez-vous des difficultés à dormir ? Je peux vous donner des conseils pour mieux dormir.");
        knowledgeBase.put("insomnie", "L'insomnie peut avoir plusieurs causes. Essayez de maintenir une routine régulière et consultez un professionnel si ça persiste.");

        // Sport & Exercice
        knowledgeBase.put("sport", "L'activité physique est excellente pour la santé ! Je recommande :\n" +
                "- 30 minutes d'activité modérée par jour\n" +
                "- Marche, course, vélo, natation\n" +
                "- Commencez progressivement si vous débutez");

        knowledgeBase.put("exercice", "Quel type d'exercice vous intéresse ? Cardio, musculation, yoga ?");
        knowledgeBase.put("fitness", "Le fitness combine cardio et renforcement musculaire. Parfait pour la santé globale !");

        // Nutrition
        knowledgeBase.put("alimentation", "Une alimentation équilibrée comprend :\n" +
                "- Fruits et légumes variés\n" +
                "- Protéines (viande, poisson, légumineuses)\n" +
                "- Féculents complets\n" +
                "- Bonne hydratation (1.5-2L d'eau/jour)");

        knowledgeBase.put("nutrition", "La nutrition est la base de la santé. Que souhaitez-vous savoir ?");
        knowledgeBase.put("régime", "Je vous conseille d'adopter une alimentation équilibrée plutôt qu'un régime strict. Consultez un nutritionniste pour un plan personnalisé.");

        // Stress & Bien-être
        knowledgeBase.put("stress", "Pour gérer le stress :\n" +
                "- Pratiquez la respiration profonde\n" +
                "- Faites des pauses régulières\n" +
                "- Essayez la méditation ou le yoga\n" +
                "- Parlez-en à quelqu'un de confiance");

        knowledgeBase.put("anxiété", "L'anxiété est normale, mais si elle persiste, consultez un professionnel. En attendant, essayez des exercices de relaxation.");
        knowledgeBase.put("méditation", "La méditation aide à calmer l'esprit. Commencez par 5 minutes par jour et augmentez progressivement.");

        // Au revoir
        knowledgeBase.put("merci", "Avec plaisir ! N'hésitez pas si vous avez d'autres questions.");
        knowledgeBase.put("au revoir", "Au revoir ! À bientôt et prenez soin de vous !");
        knowledgeBase.put("bye", "À bientôt ! Restez en bonne santé !");
    }

    public String generateResponse(String userMessage){
        if(userMessage == null || userMessage.trim().isEmpty()){
            return "Je n'ai pas compris votre message. Pouvez-vous reformuler ?";
        }

        String messageLower = userMessage.toLowerCase().trim();

        for (Map.Entry<String, String> entry : knowledgeBase.entrySet()) {
            if (messageLower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "Je ne suis pas sûr de bien comprendre. Pouvez-vous être plus précis ? " +
                "Je peux vous aider sur des sujets comme le sommeil, le sport, la nutrition ou la gestion du stress.";
    }

    public void addKnowledge(String keyword, String Response){
        knowledgeBase.put(keyword.toLowerCase(), Response);
    }
}
