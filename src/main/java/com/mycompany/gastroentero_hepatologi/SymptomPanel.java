package com.mycompany.gastroentero_hepatologi;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SymptomPanel extends JPanel {
    private static final Color CARD = Color.WHITE;
    private static final Color BORDER = new Color(219, 225, 235);
    private static final Color PRIMARY = new Color(55, 93, 220);
    private static final Color TEXT_DARK = new Color(33, 40, 54);
    private static final Color TEXT_MUTED = new Color(98, 107, 126);

    private final MainFrame mainFrame;
    private final InferenceEngine engine;
    private final Map<String, JCheckBox> checkboxes = new HashMap<>();
    private JLabel selectedCountLabel;

    public SymptomPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.engine = mainFrame.getEngine();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 12));
        setPreferredSize(new Dimension(300, 400));
        setBackground(CARD);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(CARD);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        JLabel sectionTitle = new JLabel("Input Gejala");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_DARK);
        JLabel sectionDesc = new JLabel("Centang gejala yang sedang dialami pasien");
        sectionDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sectionDesc.setForeground(TEXT_MUTED);

        JPanel titleWrapper = new JPanel();
        titleWrapper.setLayout(new BoxLayout(titleWrapper, BoxLayout.Y_AXIS));
        titleWrapper.setOpaque(false);
        titleWrapper.add(sectionTitle);
        titleWrapper.add(Box.createVerticalStrut(3));
        titleWrapper.add(sectionDesc);

        selectedCountLabel = new JLabel("Dipilih: 0 gejala");
        selectedCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        selectedCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        selectedCountLabel.setOpaque(true);
        selectedCountLabel.setBackground(new Color(230, 238, 255));
        selectedCountLabel.setForeground(PRIMARY);
        selectedCountLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        topPanel.add(titleWrapper, BorderLayout.WEST);
        topPanel.add(selectedCountLabel, BorderLayout.EAST);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 8, 2));
        checkboxPanel.setOpaque(true);
        checkboxPanel.setBackground(CARD);

        for (Symptom symptom : engine.getAllSymptoms()) {
            JCheckBox cb = new JCheckBox(symptom.getQuestion(), false);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setForeground(TEXT_DARK);
            cb.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 242, 247)),
                    BorderFactory.createEmptyBorder(8, 6, 8, 6)
            ));
            cb.setFocusPainted(false);
            cb.setOpaque(true);
            cb.setBackground(CARD);
            cb.addActionListener(e -> updateSelectedCount());
            checkboxes.put(symptom.getId(), cb);
            checkboxPanel.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.getViewport().setBackground(CARD);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(true);
        centerPanel.setBackground(CARD);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(CARD);

        JButton diagnoseBtn = new JButton("Diagnosa Sekarang");
        diagnoseBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        diagnoseBtn.setFocusPainted(false);
        diagnoseBtn.setBackground(PRIMARY);
        diagnoseBtn.setForeground(Color.WHITE);
        diagnoseBtn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        diagnoseBtn.addActionListener(e -> onDiagnose());
        buttonPanel.add(diagnoseBtn);

        JButton resetBtn = new JButton("Reset Pilihan");
        resetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resetBtn.setFocusPainted(false);
        resetBtn.setBackground(new Color(242, 244, 249));
        resetBtn.setForeground(TEXT_DARK);
        resetBtn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        resetBtn.addActionListener(e -> resetCheckboxes());
        buttonPanel.add(resetBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onDiagnose() {
        Map<String, Boolean> symptoms = new HashMap<>();
        for (Map.Entry<String, JCheckBox> entry : checkboxes.entrySet()) {
            symptoms.put(entry.getKey(), entry.getValue().isSelected());
        }
        mainFrame.performDiagnosis(symptoms);
    }

    public void resetCheckboxes() {
        for (JCheckBox cb : checkboxes.values()) {
            cb.setSelected(false);
        }
        updateSelectedCount();
    }

    private void updateSelectedCount() {
        int selected = 0;
        for (JCheckBox cb : checkboxes.values()) {
            if (cb.isSelected()) {
                selected++;
            }
        }
        selectedCountLabel.setText("Dipilih: " + selected + " gejala");
    }
}

