package com.BotIntelligent.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KnowledgeBaseService {

    private final Map<String, List<QAPair>> faqDatabase;
    private final Random random;

    public KnowledgeBaseService() {
        this.faqDatabase = new HashMap<>();
        this.random = new Random();
        initializeFAQ();
    }

    private void initializeFAQ() {
        // Sommeil
        addFAQ("sommeil",
                "combien d'heures de sommeil",
                "L'adulte moyen a besoin de 7 à 9 heures de sommeil par nuit. Les besoins varient selon l'âge et l'individu. 😴"
        );
        addFAQ("sommeil",
                "insomnie que faire",
                "Pour l'insomnie : évitez les écrans 1h avant le coucher, gardez la chambre fraîche (18-20°C), créez une routine relaxante (lecture, méditation). Si ça persiste, consultez un médecin. 🌙"
        );
        addFAQ("sommeil",
                "réveil fatigué",
                "Se réveiller fatigué peut indiquer un sommeil de mauvaise qualité. Vérifiez : votre matelas, les bruits ambiants, la température de la chambre, et essayez d'avoir des horaires réguliers. ⏰"
        );

        // Sport
        addFAQ("sport",
                "combien de sport par semaine",
                "L'OMS recommande au minimum 150 minutes d'activité modérée par semaine, soit environ 30 minutes 5 jours sur 7. Pour des bénéfices optimaux : 300 minutes/semaine ! 💪"
        );
        addFAQ("sport",
                "sport débutant",
                "Pour débuter : commencez par la marche rapide (30 min/jour), puis ajoutez du cardio léger (vélo, natation). Augmentez progressivement l'intensité. L'important : la régularité ! 🚶"
        );
        addFAQ("sport",
                "perdre du poids",
                "Pour perdre du poids sainement : combinez cardio (course, vélo) 3-4x/semaine + musculation 2-3x/semaine + alimentation équilibrée avec déficit calorique modéré (300-500 cal/jour). 📉"
        );
        addFAQ("sport",
                "musculation",
                "La musculation : excellente pour la santé ! Renforce les os, améliore le métabolisme, sculpte le corps. Commencez avec des poids légers, apprenez la bonne technique, progressez graduellement. 🏋️"
        );

        // Nutrition
        addFAQ("nutrition",
                "alimentation équilibrée",
                "Assiette équilibrée : 1/2 légumes, 1/4 protéines (viande, poisson, légumineuses), 1/4 féculents complets + bonnes graisses (huile d'olive, avocat, noix). Variez les couleurs ! 🥗"
        );
        addFAQ("nutrition",
                "calories par jour",
                "Besoins caloriques moyens : femme ~2000 kcal/jour, homme ~2500 kcal/jour. Varient selon l'âge, l'activité physique et les objectifs. Pour perdre du poids : déficit de 300-500 kcal/jour. 🍽️"
        );
        addFAQ("nutrition",
                "protéines",
                "Apport protéines recommandé : 0.8-1g par kg de poids corporel (1.6-2.2g si musculation). Sources : viande, poisson, œufs, légumineuses, tofu, produits laitiers. 🍗"
        );
        addFAQ("nutrition",
                "eau boire",
                "Hydratation : visez 1.5 à 2L d'eau par jour, plus si sport intense ou forte chaleur. Écoutez votre soif, et vérifiez la couleur de vos urines (clair = bien hydraté). 💧"
        );

        // Stress
        addFAQ("stress",
                "gérer stress",
                "Techniques anti-stress efficaces : respiration profonde (4-7-8), exercice physique, méditation, temps dans la nature, limiter la caféine, parler à quelqu'un de confiance. 🧘"
        );
        addFAQ("stress",
                "anxiété",
                "Pour l'anxiété : identifiez les déclencheurs, pratiquez la pleine conscience, limitez l'alcool et la caféine, maintenez une routine de sommeil, et n'hésitez pas à consulter un professionnel. 💙"
        );
        addFAQ("stress",
                "respiration",
                "Exercice de respiration 4-7-8 : Inspirez par le nez (4 sec), retenez (7 sec), expirez par la bouche (8 sec). Répétez 4 fois. Calme instantanément le système nerveux ! 🌬️"
        );

        // Motivation
        addFAQ("motivation",
                "rester motivé",
                "Pour maintenir la motivation : fixez des objectifs SMART (Spécifiques, Mesurables, Atteignables, Réalistes, Temporels), suivez vos progrès, célébrez les petites victoires ! 🎯"
        );
        addFAQ("motivation",
                "pas envie",
                "Manque d'envie ? Normal ! La discipline bat la motivation. Créez une routine, commencez petit (juste 5 min), utilisez la règle des 2 minutes : tout est plus facile une fois commencé. 🚀"
        );
        addFAQ("motivation",
                "objectifs",
                "Fixer des objectifs efficaces : Spécifiques (pas 'être en forme' mais 'courir 5km'), Mesurables, avec une date limite, et décomposés en petites étapes réalisables. 📊"
        );

        // Santé mentale
        addFAQ("sante_mentale",
                "dépression",
                "Si vous pensez souffrir de dépression : consultez un médecin ou psychologue. Ce n'est PAS une faiblesse, c'est une maladie qui se soigne. Vous méritez d'aller mieux. 💚"
        );
        addFAQ("sante_mentale",
                "psychologue",
                "Consulter un psychologue : aucune honte ! Comme pour le corps, l'esprit mérite des soins. Thérapies efficaces : TCC, EMDR, thérapie d'acceptation. Votre santé mentale compte ! 🧠"
        );

        // Productivité
        addFAQ("productivite",
                "technique pomodoro",
                "Technique Pomodoro : 25 min de travail concentré + 5 min de pause. Après 4 pomodoros, pause longue (15-30 min). Excellente pour la concentration et éviter le burn-out ! ⏲️"
        );
        addFAQ("productivite",
                "concentration",
                "Améliorer la concentration : éliminez les distractions (téléphone en mode avion), travaillez en blocs de temps, une tâche à la fois, faites des vraies pauses, dormez suffisamment. 🎯"
        );

        // ========== PROGRAMMATION (NOUVEAU) ==========
        addFAQ("programmation",
                "apprendre programmer",
                "Pour apprendre : choisissez un langage (Python pour débuter), suivez un cours (FreeCodeCamp, OpenClassrooms), pratiquez avec projets ! 💻"
        );
        addFAQ("programmation",
                "meilleur langage",
                "Pas de 'meilleur' ! Python = polyvalent, JavaScript = web, Java = entreprise, C++ = performances. Selon votre projet ! 🚀"
        );

        // ========== CARRIÈRE (NOUVEAU) ==========
        addFAQ("carriere",
                "premier emploi",
                "1er emploi : CV solide + projets perso, LinkedIn actif, candidatures ciblées, réseau (meetups). Persévérez ! 💼"
        );
        addFAQ("cv",
                "bon cv",
                "CV efficace : 1-2 pages, résultats quantifiés, mots-clés du poste, projets concrets, zéro faute ! 📄"
        );
        addFAQ("entretien",
                "questions entretien",
                "Questions fréquentes : Présentez-vous, Qualités/défauts, Pourquoi ce poste, Projet marquant. Préparez avec exemples ! 🎯"
        );

        // ========== FINANCES (NOUVEAU) ==========
        addFAQ("budget",
                "économiser argent",
                "Épargne : suivez dépenses, règle 50/30/20, automatisez virements, évitez achats impulsifs (24h réflexion). 💰"
        );
        addFAQ("investissement",
                "commencer investir",
                "Investissement : 1) Formez-vous, 2) Fonds d'urgence, 3) ETF diversifiés, 4) Horizon long terme. 📈"
        );

        // ========== ÉTUDES (NOUVEAU) ==========
        addFAQ("etudes",
                "mieux étudier",
                "Études efficaces : Pomodoro, résumés manuscrits, enseignez à quelqu'un, testez-vous, espacez révisions. 📚"
        );
        addFAQ("examens",
                "stress examens",
                "Anti-stress : préparation régulière, sommeil suffisant, respiration, arrivez en avance. Vous êtes prêt ! 💪"
        );

        // ========== TECHNOLOGIE (NOUVEAU) ==========
        addFAQ("web",
                "développeur web",
                "Dev web : 1) HTML/CSS/JS, 2) Framework (React/Angular), 3) Backend (Node/Java), 4) BDD, 5) Git. Projets +++  🌐"
        );
        addFAQ("intelligence_artificielle",
                "apprendre ia",
                "IA/ML : 1) Maths (stats), 2) Python, 3) Bibliothèques (NumPy, Pandas), 4) Cours Coursera, 5) Projets Kaggle. 🤖"
        );

        // Citations motivantes
        addMotivationalQuotes();
    }

    private void addMotivationalQuotes() {
        List<String> quotes = Arrays.asList(
                "💪 'Le succès, c'est tomber sept fois et se relever huit fois.' - Proverbe japonais",
                "🌟 'La seule façon d'accomplir de grandes choses est d'aimer ce que l'on fait.' - Steve Jobs",
                "🚀 'L'action est la clé fondamentale de tout succès.' - Pablo Picasso",
                "🎯 'Un voyage de mille lieues commence toujours par un premier pas.' - Lao Tseu",
                "💡 'Le meilleur moment pour planter un arbre était il y a 20 ans. Le deuxième meilleur moment est maintenant.' - Proverbe chinois",
                "⭐ 'Vous êtes plus courageux que vous ne le croyez, plus fort que vous ne le semblez.' - A.A. Milne",
                "🌈 'Les difficultés préparent souvent les gens ordinaires à un destin extraordinaire.' - C.S. Lewis",
                "🔥 'Ne comptez pas les jours, faites que les jours comptent.' - Muhammad Ali"
        );

        for (String quote : quotes) {
            addFAQ("motivation", "citation motivation", quote);
        }
    }

    private void addFAQ(String category, String question, String answer) {
        faqDatabase.putIfAbsent(category, new ArrayList<>());
        faqDatabase.get(category).add(new QAPair(question, answer));
    }

    /**
     * Recherche une réponse dans la FAQ
     */
    public String searchFAQ(String category, String userMessage) {
        List<QAPair> categoryFAQ = faqDatabase.get(category);
        if (categoryFAQ == null) {
            return null;
        }

        String messageLower = userMessage.toLowerCase();
        List<QAPair> matches = new ArrayList<>();

        // Rechercher les correspondances
        for (QAPair pair : categoryFAQ) {
            String[] questionWords = pair.question.split(" ");
            int matchCount = 0;

            for (String word : questionWords) {
                if (messageLower.contains(word)) {
                    matchCount++;
                }
            }

            // Si au moins 50% des mots correspondent
            if (matchCount >= questionWords.length * 0.5) {
                matches.add(pair);
            }
        }

        // Retourner une réponse aléatoire parmi les matches
        if (!matches.isEmpty()) {
            return matches.get(random.nextInt(matches.size())).answer;
        }

        return null;
    }

    /**
     * Obtenir une citation motivante aléatoire
     */
    public String getRandomQuote() {
        List<QAPair> quotes = faqDatabase.get("motivation");
        if (quotes != null && !quotes.isEmpty()) {
            // Filtrer pour ne garder que les citations
            List<QAPair> onlyQuotes = new ArrayList<>();
            for (QAPair pair : quotes) {
                if (pair.question.equals("citation motivation")) {
                    onlyQuotes.add(pair);
                }
            }
            if (!onlyQuotes.isEmpty()) {
                return onlyQuotes.get(random.nextInt(onlyQuotes.size())).answer;
            }
        }
        return "Chaque jour est une nouvelle chance de progresser ! 🌟";
    }

    // Classe interne pour stocker question/réponse
    private static class QAPair {
        String question;
        String answer;

        QAPair(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }
}