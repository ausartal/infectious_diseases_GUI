package com.mycompany.gastroentero_hepatologi;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Gastroentero_Hepatologi {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {
            }

            new MainFrame().setVisible(true);
        });
    }
}
