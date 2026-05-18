package com.mycompany.gastroentero_hepatologi;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Disease {
    private final String name;
    private final String category;
    private final List<String> symptomIds;
    private final Set<String> criticalSymptomIds;

    public Disease(String name, String category, List<String> symptomIds, Set<String> criticalSymptomIds) {
        this.name = name;
        this.category = category;
        this.symptomIds = symptomIds;
        this.criticalSymptomIds = criticalSymptomIds;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public List<String> getSymptomIds() { return Collections.unmodifiableList(symptomIds); }
    public Set<String> getCriticalSymptomIds() { return Collections.unmodifiableSet(criticalSymptomIds); }
}
