package com.mycompany.gastroentero_hepatologi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DecisionTreeNode {
    private final String label;
    private final Disease disease;
    private final List<DecisionTreeNode> children = new ArrayList<>();

    public DecisionTreeNode(String label) {
        this(label, null);
    }

    public DecisionTreeNode(String label, Disease disease) {
        this.label = label;
        this.disease = disease;
    }

    public String getLabel() {
        return label;
    }

    public Disease getDisease() {
        return disease;
    }

    public void addChild(DecisionTreeNode child) {
        children.add(child);
    }

    public List<DecisionTreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean isLeaf() {
        return disease != null;
    }
}
