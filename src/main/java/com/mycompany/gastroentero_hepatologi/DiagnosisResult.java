package com.mycompany.gastroentero_hepatologi;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosisResult {
    private final String kategori;
    private final String diagnosis;
    private final double percentage;
    private final List<String> matchedSymptoms;
    private final List<String> path;
    private final String reasoning;

    public DiagnosisResult(String kategori,
                           String diagnosis,
                           double percentage,
                           List<String> matchedSymptoms,
                           List<String> path,
                           String reasoning) {
        this.kategori = kategori;
        this.diagnosis = diagnosis;
        this.percentage = percentage;
        this.matchedSymptoms = matchedSymptoms;
        this.path = path;
        this.reasoning = reasoning;
    }

    public String getKategori() { return kategori; }
    public String getDiagnosis() { return diagnosis; }
    public double getPercentage() { return percentage; }
    public List<String> getMatchedSymptoms() { return Collections.unmodifiableList(matchedSymptoms); }
    public List<String> getPath() { return Collections.unmodifiableList(path); }
    public String getReasoning() { return reasoning; }

    public String getConfidence() {
        return String.format("%.0f%%", percentage * 100);
    }

    public String toJson() {
        String matched = matchedSymptoms.stream()
                .map(this::quote)
                .collect(Collectors.joining(", "));

        String traversalPath = path.stream()
                .map(this::quote)
                .collect(Collectors.joining(", "));

        return "{\n"
                + "  \"kategori\": " + quote(kategori) + ",\n"
                + "  \"diagnosis\": " + quote(diagnosis) + ",\n"
                + "  \"confidence\": " + quote(getConfidence()) + ",\n"
                + "  \"path\": [" + traversalPath + "],\n"
                + "  \"matched_symptoms\": [" + matched + "],\n"
                + "  \"reasoning\": " + quote(reasoning) + "\n"
                + "}";
    }

    private String quote(String value) {
        String safe = value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + safe + "\"";
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", diagnosis, kategori, getConfidence());
    }
}
