package com.mycompany.gastroentero_hepatologi;

import java.util.*;

public class InferenceEngine {
    private static final double MIN_THRESHOLD = 0.60;
    private static final String ROOT_LABEL = "Diagnosa Penyakit";
    private static final String DENGUE = "Demam Berdarah";

    private final KnowledgeBase kb;

    public InferenceEngine(KnowledgeBase kb) {
        this.kb = kb;
    }

    public List<DiagnosisResult> diagnose(Map<String, Boolean> symptomsSelected) {
        DecisionTreeNode categoryNode = traverseCategory(symptomsSelected);
        DiagnosisResult result = traverseDisease(categoryNode, symptomsSelected);
        if (result == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(result);
    }

    public DecisionTreeNode traverseCategory(Map<String, Boolean> symptomsSelected) {
        DecisionTreeNode root = kb.getRootNode();
        List<DecisionTreeNode> categories = root.getChildren();
        if (categories.isEmpty()) {
            return null;
        }

        DecisionTreeNode bestCategory = categories.get(0);
        double bestScore = scoreCategory(bestCategory, symptomsSelected);
        int bestMatched = countCategoryMatches(bestCategory, symptomsSelected);

        for (int i = 1; i < categories.size(); i++) {
            DecisionTreeNode candidate = categories.get(i);
            double candidateScore = scoreCategory(candidate, symptomsSelected);
            int candidateMatched = countCategoryMatches(candidate, symptomsSelected);

            if (candidateScore > bestScore
                    || (Double.compare(candidateScore, bestScore) == 0 && candidateMatched > bestMatched)) {
                bestCategory = candidate;
                bestScore = candidateScore;
                bestMatched = candidateMatched;
            }
        }

        return bestCategory;
    }

    public DiagnosisResult traverseDisease(DecisionTreeNode categoryNode, Map<String, Boolean> symptomsSelected) {
        if (categoryNode == null) {
            return null;
        }

        List<DecisionTreeNode> diseaseNodes = categoryNode.getChildren();
        if (diseaseNodes.isEmpty()) {
            return null;
        }

        Disease bestDisease = null;
        List<String> bestMatchedSymptoms = new ArrayList<>();
        double bestScore = -1.0;

        for (DecisionTreeNode diseaseNode : diseaseNodes) {
            Disease disease = diseaseNode.getDisease();
            List<String> matched = findMatchedSymptoms(disease, symptomsSelected);
            double score = disease.getSymptomIds().isEmpty()
                    ? 0.0
                    : (double) matched.size() / disease.getSymptomIds().size();

            if (bestDisease == null || score > bestScore) {
                bestDisease = disease;
                bestScore = score;
                bestMatchedSymptoms = matched;
                continue;
            }

            if (Double.compare(score, bestScore) == 0) {
                List<String> currentMatched = matched;
                if (isTieWinner(disease, currentMatched, bestDisease, bestMatchedSymptoms)) {
                    bestDisease = disease;
                    bestMatchedSymptoms = currentMatched;
                }
            }
        }

        String diagnosisName = bestScore >= MIN_THRESHOLD
                ? bestDisease.getName()
                : "Belum dapat ditentukan";

        List<String> path = new ArrayList<>();
        path.add(ROOT_LABEL);
        path.add(categoryNode.getLabel());
        if (bestScore >= MIN_THRESHOLD) {
            path.add(bestDisease.getName());
        }

        String reasoning = "Tree traversal based on symptom matching";
        return new DiagnosisResult(
                categoryNode.getLabel(),
                diagnosisName,
                Math.max(0.0, bestScore),
                bestMatchedSymptoms,
                path,
                reasoning
        );
    }

    private double scoreCategory(DecisionTreeNode categoryNode, Map<String, Boolean> symptomsSelected) {
        double best = 0.0;
        for (DecisionTreeNode diseaseNode : categoryNode.getChildren()) {
            Disease disease = diseaseNode.getDisease();
            int matched = findMatchedSymptoms(disease, symptomsSelected).size();
            double score = disease.getSymptomIds().isEmpty() ? 0.0 : (double) matched / disease.getSymptomIds().size();
            if (score > best) {
                best = score;
            }
        }
        return best;
    }

    private int countCategoryMatches(DecisionTreeNode categoryNode, Map<String, Boolean> symptomsSelected) {
        Set<String> symptomIds = new HashSet<>();
        for (DecisionTreeNode diseaseNode : categoryNode.getChildren()) {
            symptomIds.addAll(diseaseNode.getDisease().getSymptomIds());
        }

        int matched = 0;
        for (String id : symptomIds) {
            if (symptomsSelected.getOrDefault(id, false)) {
                matched++;
            }
        }
        return matched;
    }

    private List<String> findMatchedSymptoms(Disease disease, Map<String, Boolean> symptomsSelected) {
        List<String> matched = new ArrayList<>();
        for (String symptomId : disease.getSymptomIds()) {
            if (symptomsSelected.getOrDefault(symptomId, false)) {
                matched.add(symptomId);
            }
        }
        return matched;
    }

    private boolean isTieWinner(Disease candidate,
                                List<String> candidateMatched,
                                Disease currentBest,
                                List<String> currentBestMatched) {
        boolean candidateCritical = hasCriticalMatch(candidate, candidateMatched);
        boolean currentCritical = hasCriticalMatch(currentBest, currentBestMatched);

        if (DENGUE.equals(candidate.getName()) && candidateCritical && !currentCritical) {
            return true;
        }
        if (DENGUE.equals(currentBest.getName()) && currentCritical && !candidateCritical) {
            return false;
        }

        if (candidateMatched.size() != currentBestMatched.size()) {
            return candidateMatched.size() > currentBestMatched.size();
        }

        return candidate.getName().compareToIgnoreCase(currentBest.getName()) < 0;
    }

    private boolean hasCriticalMatch(Disease disease, List<String> matchedSymptoms) {
        for (String critical : disease.getCriticalSymptomIds()) {
            if (matchedSymptoms.contains(critical)) {
                return true;
            }
        }
        return false;
    }

    public List<Symptom> getAllSymptoms() {
        return kb.getSymptoms();
    }
}
