package com.student;

import com.student.dao.DatabaseHelper;
import com.student.view.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * 学生信息管理系统 - 主入口
 */
public class StudentManagementApp {
    private static final Logger logger = LoggerFactory.getLogger(StudentManagementApp.class);

    public static void main(String[] args) {
        // 设置系统外观
        try {
            // 尝试使用系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("无法设置系统外观，使用默认外观", e);
        }

        // 设置全局字体（可选，改善中文显示）
        setGlobalFont();

        // 初始化数据库
        try {
            DatabaseHelper.getInstance();
            logger.info("数据库初始化成功");
        } catch (Exception e) {
            logger.error("数据库初始化失败", e);
            JOptionPane.showMessageDialog(null,
                    "数据库初始化失败：" + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建并显示主窗口
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                logger.info("主窗口已显示");
            } catch (Exception e) {
                logger.error("创建主窗口失败", e);
                JOptionPane.showMessageDialog(null,
                        "程序启动失败：" + e.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * 设置全局字体，改善中文显示
     */
    private static void setGlobalFont() {
        // 设置默认字体为微软雅黑（Windows）或其他中文字体
        String fontFamily = "Microsoft YaHei";

        // 检查字体是否存在，不存在则使用 Dialog
        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        boolean hasFont = false;
        for (String font : availableFonts) {
            if (font.equals(fontFamily)) {
                hasFont = true;
                break;
            }
        }
        if (!hasFont) {
            fontFamily = "Dialog";
        }

        Font defaultFont = new Font(fontFamily, Font.PLAIN, 12);

        // 设置各种组件的默认字体
        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", defaultFont);
        UIManager.put("OptionPane.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("RadioButtonMenuItem.font", defaultFont);
        UIManager.put("CheckBoxMenuItem.font", defaultFont);
        UIManager.put("PopupMenu.font", defaultFont);
        UIManager.put("TabbedPane.font", defaultFont);
        UIManager.put("TabbedPane.tabFont", defaultFont);
        UIManager.put("TitledBorder.font", defaultFont);
    }
}
