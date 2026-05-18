package com.mycompany.gastroentero_hepatologi;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MainFrame extends JFrame {
    private static final Color BG = new Color(245, 247, 252);
    private static final Color CARD = Color.WHITE;
    private static final Color BORDER = new Color(219, 225, 235);
    private static final Color PRIMARY = new Color(55, 93, 220);
    private static final Color TEXT_DARK = new Color(33, 40, 54);
    private static final Color TEXT_MUTED = new Color(98, 107, 126);

    private final InferenceEngine engine;
    private SymptomPanel symptomPanel;
    private ResultPanel resultPanel;

    public MainFrame() {
        engine = new InferenceEngine(new KnowledgeBase());
        initComponents();
    }

    public InferenceEngine getEngine() {
        return engine;
    }

    private void initComponents() {
        setTitle("Sistem Pakar Diagnosa Penyakit");
        setSize(1100, 700);
        setMinimumSize(new Dimension(960, 620));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel headerPanel = new JPanel(new BorderLayout(14, 0));
        headerPanel.setBackground(CARD);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 0, BORDER),
            BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(PRIMARY);
        accentBar.setPreferredSize(new Dimension(8, 1));

        JLabel titleLabel = new JLabel("Sistem Pakar Diagnosa Penyakit (Decision Tree)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        JLabel subtitleLabel = new JLabel("Pilih gejala di panel kiri lalu tekan Diagnosa untuk melihat hasil analisis.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_MUTED);

        JLabel badge = new JLabel("Decision Support");
        badge.setOpaque(true);
        badge.setBackground(new Color(230, 238, 255));
        badge.setForeground(PRIMARY);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);
        titleGroup.add(titleLabel);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(subtitleLabel);
        titleGroup.add(Box.createVerticalStrut(10));
        titleGroup.add(badge);

        headerPanel.add(accentBar, BorderLayout.WEST);
        headerPanel.add(titleGroup, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(420);
        splitPane.setResizeWeight(0.42);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(BorderFactory.createLineBorder(BORDER));
        splitPane.setBackground(BG);

        symptomPanel = new SymptomPanel(this);
        resultPanel = new ResultPanel(this);

        splitPane.setLeftComponent(symptomPanel);
        splitPane.setRightComponent(resultPanel);

        content.add(headerPanel, BorderLayout.NORTH);
        content.add(splitPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG);
        JLabel footerLabel = new JLabel("Tip: gunakan Reset Diagnosa dari menu File untuk mulai ulang.");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(TEXT_MUTED);
        footer.add(footerLabel, BorderLayout.WEST);

        content.add(footer, BorderLayout.SOUTH);

        setContentPane(content);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        JMenu fileMenu = new JMenu("File");
        JMenuItem refreshItem = new JMenuItem("Reset Diagnosa");
        refreshItem.addActionListener(e -> {
            symptomPanel.resetCheckboxes();
            resultPanel.resetState();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Bantuan");
        JMenuItem aboutItem = new JMenuItem("Tentang");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
            "Aplikasi sistem pakar berbasis decision tree untuk kategori Infeksi dan Non-Infeksi.",
                "Tentang Aplikasi",
                JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public void performDiagnosis(Map<String, Boolean> symptoms) {
        List<DiagnosisResult> results = engine.diagnose(symptoms);
        int selectedCount = (int) symptoms.values().stream().filter(Boolean::booleanValue).count();
        String selectedIds = symptoms.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.joining(", "));
        resultPanel.updateResults(results, selectedCount, selectedIds);
    }
}

