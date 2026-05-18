package com.mycompany.gastroentero_hepatologi;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    private static final Color CARD = Color.WHITE;
    private static final Color BORDER = new Color(219, 225, 235);
    private static final Color PRIMARY = new Color(55, 93, 220);
    private static final Color TEXT_DARK = new Color(33, 40, 54);
    private static final Color TEXT_MUTED = new Color(98, 107, 126);

    private JTextPane resultPane;
    private JLabel confidenceLabel;

    public ResultPanel(MainFrame mainFrame) {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 12));
        setBackground(CARD);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(true);
        titlePanel.setBackground(CARD);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        JLabel titleLabel = new JLabel("Hasil Diagnosis");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_DARK);
        JLabel hintLabel = new JLabel("Sistem menampilkan 3 hasil teratas berdasarkan kecocokan gejala");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hintLabel.setForeground(TEXT_MUTED);

        JPanel titleWrap = new JPanel();
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));
        titleWrap.setOpaque(false);
        titleWrap.add(titleLabel);
        titleWrap.add(Box.createVerticalStrut(3));
        titleWrap.add(hintLabel);
        titlePanel.add(titleWrap, BorderLayout.WEST);

        add(titlePanel, BorderLayout.NORTH);

        resultPane = new JTextPane();
        resultPane.setContentType("text/html");
        resultPane.setEditable(false);
        resultPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        resultPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.getViewport().setBackground(CARD);
        add(scrollPane, BorderLayout.CENTER);

        confidenceLabel = new JLabel("Confidence: -");
        confidenceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confidenceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confidenceLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        confidenceLabel.setOpaque(true);
        confidenceLabel.setBackground(new Color(230, 238, 255));
        confidenceLabel.setForeground(PRIMARY);
        add(confidenceLabel, BorderLayout.SOUTH);

        resetState();
    }

    public void updateResults(List<DiagnosisResult> results, int selectedCount, String selectedIds) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Segoe UI; font-size:13px; margin:8px; color:#212836;'>");
        html.append("<div style='margin-bottom:10px;'><b>Total gejala dipilih:</b> ").append(selectedCount).append("</div>");

        if (results.isEmpty()) {
            html.append("<div style='padding:12px; border:1px solid #E1E4EA; background:#F8F9FB; border-radius:10px;'>");
            html.append("<div style='font-size:15px; font-weight:700; margin-bottom:6px; color:#212836;'>Belum ada diagnosis kuat</div>");
            html.append("<div>Tidak ada penyakit yang melewati threshold 60%. Coba pilih gejala tambahan untuk analisis lebih akurat.</div>");
            html.append("</div>");
            if (selectedIds != null && !selectedIds.isBlank()) {
                html.append("<div style='margin-top:10px; color:#606884;'>Gejala terpilih: ").append(selectedIds).append("</div>");
            }
            confidenceLabel.setText("Confidence: None");
        } else {
            DiagnosisResult top = results.get(0);

            html.append("<div style='margin-bottom:10px; padding:12px; border:1px solid #D6DEFF; background:#F4F6FF; border-radius:10px;'>");
            html.append("<div style='font-size:16px; font-weight:700; margin-bottom:2px; color:#212836;'>Hasil Tree Traversal</div>");
            html.append("<div style='font-size:14px;'><b>Kategori:</b> ").append(top.getKategori()).append("</div>");
            html.append("<div style='font-size:14px;'><b>Diagnosis:</b> ").append(top.getDiagnosis()).append("</div>");
            html.append("<div style='margin-top:2px;'><b>Confidence:</b> ").append(top.getConfidence()).append("</div>");
            html.append("</div>");

            html.append("<div style='margin-bottom:8px; padding:10px; border:1px solid #E2E5EC; border-radius:10px; background:#FFFFFF;'>");
            html.append("<div style='font-weight:700; color:#212836;'>Path</div>");
            html.append("<div style='color:#5E667E;'>").append(top.getPath()).append("</div>");
            html.append("</div>");

            html.append("<div style='margin-bottom:8px; padding:10px; border:1px solid #E2E5EC; border-radius:10px; background:#FFFFFF;'>");
            html.append("<div style='font-weight:700; color:#212836;'>Matched Symptoms</div>");
            html.append("<div style='color:#5E667E;'>").append(top.getMatchedSymptoms()).append("</div>");
            html.append("</div>");

            html.append("<div style='margin-bottom:8px; padding:10px; border:1px solid #E2E5EC; border-radius:10px; background:#FFFFFF;'>");
            html.append("<div style='font-weight:700; color:#212836;'>JSON Output</div>");
            html.append("<pre style='margin-top:6px; background:#F8F9FB; border:1px solid #E1E4EA; padding:8px; overflow:auto;'>")
                    .append(escapeHtml(top.toJson()))
                    .append("</pre>");
            html.append("</div>");

            if (selectedIds != null && !selectedIds.isBlank()) {
                html.append("<div style='margin-top:8px; color:#606884;'>Gejala terpilih: ").append(selectedIds).append("</div>");
            }

            confidenceLabel.setText("Confidence: " + top.getConfidence());
        }

        html.append("</body></html>");
        resultPane.setText(html.toString());
        resultPane.setCaretPosition(0);
    }

    public void resetState() {
        resultPane.setText("<html><body style='font-family:Segoe UI; font-size:13px; margin:8px; color:#212836;'>"
            + "<div style='padding:12px; border:1px solid #E1E4EA; background:#F8F9FB; border-radius:10px;'>"
            + "<div style='font-size:15px; font-weight:700; margin-bottom:6px;'>Siap untuk diagnosa</div>"
                + "<div>Pilih gejala di panel kiri lalu tekan <b>Diagnosa Sekarang</b> untuk melihat hasil.</div>"
                + "</div></body></html>");
        confidenceLabel.setText("Confidence: -");
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

