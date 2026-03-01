package com.student.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * 表格样式工具类
 */
public class TableUtils {

    /**
     * 应用统一样式到表格
     */
    public static void applyStyle(JTable table) {
        // 设置行高
        table.setRowHeight(30);

        // 启用自动创建行排序器
        table.setAutoCreateRowSorter(true);

        // 设置选择模式
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 设置选中行背景色
        table.setSelectionBackground(Theme.TABLE_SELECTION);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);

        // 设置网格线颜色
        table.setGridColor(Theme.TABLE_GRID);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // 设置表头样式
        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.FONT_BOLD);
        header.setBackground(Theme.TABLE_HEADER_BG);
        header.setForeground(Theme.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));

        // 设置默认字体
        table.setFont(Theme.FONT_DEFAULT);
        table.setForeground(Theme.TEXT_PRIMARY);

        // 设置单元格渲染器（隔行变色）
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // 设置居中渲染器（用于特定列）
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(Theme.FONT_DEFAULT);

        return;
    }

    /**
     * 应用居中渲染器到指定列
     */
    public static void setCenterRenderer(JTable table, int... columns) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(Theme.FONT_DEFAULT);

        for (int column : columns) {
            table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
        }
    }

    /**
     * 设置列宽度
     */
    public static void setColumnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * 隔行变色渲染器
     */
    static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        public AlternatingRowRenderer() {
            setFont(Theme.FONT_DEFAULT);
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                // 隔行变色
                if (row % 2 == 0) {
                    c.setBackground(Theme.BACKGROUND_PRIMARY);
                } else {
                    c.setBackground(Theme.TABLE_ROW_ALT);
                }
            }

            return c;
        }
    }

    /**
     * 创建带图标的表头渲染器（支持排序图标）
     */
    public static void applySortableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new TableColumnHeaderRenderer(table));
    }

    /**
     * 自定义表头渲染器
     */
    static class TableColumnHeaderRenderer extends DefaultTableCellRenderer {
        private final JTable table;

        public TableColumnHeaderRenderer(JTable table) {
            this.table = table;
            setHorizontalAlignment(JLabel.CENTER);
            setFont(Theme.FONT_BOLD);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(Theme.TABLE_HEADER_BG);
            c.setForeground(Theme.TEXT_PRIMARY);
            return c;
        }
    }
}
