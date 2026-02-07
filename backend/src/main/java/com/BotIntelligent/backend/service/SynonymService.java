package com.BotIntelligent.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SynonymService {

    private final Map<String, Set<String>> synonymGroups;

    public SynonymService() {
        this.synonymGroups = new HashMap<>();
        initializeSynonyms();
    }

    private void initializeSynonyms() {
        // Groupe 1 : Fatigue
        addSynonymGroup("fatigue", "fatigué", "épuisé", "crevé", "exténué", "las", "éreinté", "vanné", "mort de fatigue");

        // Groupe 2 : Stress
        addSynonymGroup("stress", "stressé", "anxieux", "angoissé", "nerveux", "tendu", "sous pression", "inquiet", "préoccupé");

        // Groupe 3 : Sommeil
        addSynonymGroup("sommeil", "dormir", "dodo", "repos", "nuit", "sieste", "roupillon", "insomnie");

        // Groupe 4 : Sport
        addSynonymGroup("sport", "exercice", "entraînement", "fitness", "gym", "musculation", "bouger", "activité physique");

        // Groupe 5 : Motivation
        addSynonymGroup("motivation", "motivé", "envie", "volonté", "détermination", "enthousiasme", "courage");

        // Groupe 6 : Démotivation
        addSynonymGroup("démotivation", "démotivé", "découragé", "sans envie", "à plat", "blasé", "lassé");

        // Groupe 7 : Tristesse
        addSynonymGroup("tristesse", "triste", "déprimé", "mélancolique", "malheureux", "cafardeux", "morose", "down");

        // Groupe 8 : Bonheur
        addSynonymGroup("bonheur", "heureux", "content", "joyeux", "ravi", "épanoui", "bien", "en forme");

        // Groupe 9 : Alimentation
        addSynonymGroup("alimentation", "manger", "nutrition", "nourriture", "bouffe", "repas", "diet", "régime");

        // Groupe 10 : Douleur
        addSynonymGroup("douleur", "mal", "souffrance", "courbatures", "douloureux", "blessure");
    }

    private void addSynonymGroup(String... words) {
        Set<String> group = new HashSet<>(Arrays.asList(words));
        for (String word : words) {
            synonymGroups.put(word.toLowerCase(), group);
        }
    }

    /**
     * Trouve tous les synonymes d'un mot
     */
    public Set<String> getSynonyms(String word) {
        return synonymGroups.getOrDefault(word.toLowerCase(), Collections.emptySet());
    }

    /**
     * Vérifie si deux mots sont synonymes
     */
    public boolean areSynonyms(String word1, String word2) {
        Set<String> synonyms = getSynonyms(word1);
        return synonyms.contains(word2.toLowerCase());
    }

    /**
     * Normalise un texte en remplaçant les synonymes par le mot principal
     */
    public String normalizeText(String text) {
        String normalized = text.toLowerCase();

        for (Map.Entry<String, Set<String>> entry : synonymGroups.entrySet()) {
            String mainWord = entry.getKey();
            Set<String> synonyms = entry.getValue();

            for (String synonym : synonyms) {
                if (!synonym.equals(mainWord)) {
                    normalized = normalized.replaceAll("\\b" + synonym + "\\b", mainWord);
                }
            }
        }

        return normalized;
    }

    /**
     * Détecte le sentiment d'un message (POSITIF, NÉGATIF, NEUTRE)
     */
    public Sentiment analyzeSentiment(String message) {
        String messageLower = message.toLowerCase();

        // Mots positifs
        List<String> positiveWords = Arrays.asList(
                "bien", "super", "génial", "excellent", "parfait", "merci", "content", "heureux",
                "ravi", "cool", "top", "formidable", "incroyable", "motivé", "énergique"
        );

        // Mots négatifs
        List<String> negativeWords = Arrays.asList(
                "mal", "mauvais", "nul", "horrible", "terrible", "triste", "déprimé", "fatigué",
                "stressé", "anxieux", "inquiet", "peur", "pas bien", "difficile", "dur", "problème"
        );

        int positiveCount = 0;
        int negativeCount = 0;

        for (String word : positiveWords) {
            if (messageLower.contains(word)) positiveCount++;
        }

        for (String word : negativeWords) {
            if (messageLower.contains(word)) negativeCount++;
        }

        if (positiveCount > negativeCount) {
            return Sentiment.POSITIF;
        } else if (negativeCount > positiveCount) {
            return Sentiment.NEGATIF;
        } else {
            return Sentiment.NEUTRE;
        }
    }

    public enum Sentiment {
        POSITIF, NEGATIF, NEUTRE
    }
}