package com.mycompany.gastroentero_hepatologi;

import java.util.*;

public class KnowledgeBase {
    private static final String ROOT_LABEL = "Diagnosa Penyakit";
    private static final String INFEKSI = "Infeksi";
    private static final String NON_INFEKSI = "Non-Infeksi";

    private List<Symptom> symptoms;
    private List<Disease> diseases;
    private DecisionTreeNode rootNode;

    public KnowledgeBase() {
        initSymptoms();
        initDiseases();
        initDecisionTree();
    }

    private void initSymptoms() {
        symptoms = Arrays.asList(
            new Symptom("INF_FLU_DEMAM", "Demam influenza", "Apakah pasien mengalami demam 37.5–39°C yang bertahap dengan menggigil ringan?"),
            new Symptom("INF_FLU_BATUK", "Batuk", "Apakah pasien batuk kering lebih dari 3x per jam dengan dahak ringan?"),
            new Symptom("INF_FLU_PILEK", "Pilek", "Apakah pasien mengalami hidung tersumbat/berair atau bersin?"),
            new Symptom("INF_FLU_TENGGOROKAN", "Sakit tenggorokan", "Apakah pasien nyeri saat menelan atau tenggorokan kemerahan?"),

            new Symptom("INF_DBD_DEMAM_TINGGI", "Demam tinggi", "Apakah pasien mengalami demam tinggi >39°C mendadak selama 2–7 hari naik-turun?"),
            new Symptom("INF_DBD_NYERI_SENDI", "Nyeri sendi/otot/tulang", "Apakah pasien mengalami nyeri sendi, otot, atau tulang?"),
            new Symptom("INF_DBD_RUAM", "Ruam kulit", "Apakah ada ruam/bintik merah yang tidak hilang saat ditekan?"),
            new Symptom("INF_DBD_MUAL", "Mual", "Apakah pasien muntah lebih dari 2x/hari atau nafsu makan turun?"),

            new Symptom("NON_DM_HAUS", "Sering haus", "Apakah pasien minum lebih dari 3 liter per hari (sering haus)?"),
            new Symptom("NON_DM_BAK", "Sering buang air kecil", "Apakah pasien buang air kecil ≥8x/hari termasuk malam hari?"),
            new Symptom("NON_DM_LELAH", "Mudah lelah", "Apakah pasien mudah lelah dengan indikasi gula darah >200 mg/dL?"),
            new Symptom("NON_DM_LUKA", "Luka sulit sembuh", "Apakah pasien memiliki luka yang sulit sembuh lebih dari 2 minggu?"),

            new Symptom("NON_HT_SAKIT_KEPALA", "Sakit kepala", "Apakah pasien sakit kepala di pagi hari terutama belakang kepala?"),
            new Symptom("NON_HT_PUSING", "Pusing", "Apakah pasien pusing berputar saat perubahan posisi?"),
            new Symptom("NON_HT_KABUR", "Penglihatan kabur", "Apakah pasien mengalami penglihatan kabur?"),
            new Symptom("NON_HT_TD", "Tekanan darah tinggi", "Apakah tekanan darah pasien ≥140/90 mmHg?"),
            new Symptom("NON_HT_MIMISAN", "Mimisan", "Apakah pasien mengalami mimisan?")
        );
    }

    private void initDiseases() {
        diseases = new ArrayList<>();

        diseases.add(new Disease(
                "Influenza",
                INFEKSI,
                Arrays.asList("INF_FLU_DEMAM", "INF_FLU_BATUK", "INF_FLU_PILEK", "INF_FLU_TENGGOROKAN"),
                Collections.emptySet()
        ));

        diseases.add(new Disease(
                "Demam Berdarah",
                INFEKSI,
                Arrays.asList("INF_DBD_DEMAM_TINGGI", "INF_DBD_NYERI_SENDI", "INF_DBD_RUAM", "INF_DBD_MUAL"),
                Collections.singleton("INF_DBD_DEMAM_TINGGI")
        ));

        diseases.add(new Disease(
                "Diabetes",
                NON_INFEKSI,
                Arrays.asList("NON_DM_HAUS", "NON_DM_BAK", "NON_DM_LELAH", "NON_DM_LUKA"),
                Collections.emptySet()
        ));

        diseases.add(new Disease(
                "Hipertensi",
                NON_INFEKSI,
                Arrays.asList("NON_HT_SAKIT_KEPALA", "NON_HT_PUSING", "NON_HT_KABUR", "NON_HT_TD", "NON_HT_MIMISAN"),
                Collections.emptySet()
        ));
    }

    private void initDecisionTree() {
        rootNode = new DecisionTreeNode(ROOT_LABEL);
        DecisionTreeNode infeksiNode = new DecisionTreeNode(INFEKSI);
        DecisionTreeNode nonInfeksiNode = new DecisionTreeNode(NON_INFEKSI);

        for (Disease disease : diseases) {
            DecisionTreeNode diseaseNode = new DecisionTreeNode(disease.getName(), disease);
            if (INFEKSI.equals(disease.getCategory())) {
                infeksiNode.addChild(diseaseNode);
            } else {
                nonInfeksiNode.addChild(diseaseNode);
            }
        }

        rootNode.addChild(infeksiNode);
        rootNode.addChild(nonInfeksiNode);
    }

    public List<Symptom> getSymptoms() { return symptoms; }
    public List<Disease> getDiseases() { return diseases; }
    public DecisionTreeNode getRootNode() { return rootNode; }
}
