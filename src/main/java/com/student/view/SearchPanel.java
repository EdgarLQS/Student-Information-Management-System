package com.student.view;

import javax.swing.*;
import java.awt.*;

/**
 * 搜索面板
 */
public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton exportButton;

    private Runnable onSearch;
    private Runnable onRefresh;
    private Runnable onAdd;
    private Runnable onEdit;
    private Runnable onDelete;
    private Runnable onExport;

    public SearchPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBorder(BorderFactory.createTitledBorder("操作区"));

        // 搜索框
        searchField = new JTextField(20);
        searchField.setToolTipText("输入学号或姓名进行搜索");

        // 搜索按钮
        searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> {
            if (onSearch != null) {
                onSearch.run();
            }
        });

        // 回车键搜索
        searchField.addActionListener(e -> {
            if (onSearch != null) {
                onSearch.run();
            }
        });

        // 刷新按钮
        refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> {
            if (onRefresh != null) {
                onRefresh.run();
            }
        });

        // 添加按钮
        addButton = new JButton("添加");
        addButton.addActionListener(e -> {
            if (onAdd != null) {
                onAdd.run();
            }
        });

        // 修改按钮
        editButton = new JButton("修改");
        editButton.addActionListener(e -> {
            if (onEdit != null) {
                onEdit.run();
            }
        });

        // 删除按钮
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> {
            if (onDelete != null) {
                onDelete.run();
            }
        });

        // 导出按钮
        exportButton = new JButton("导出");
        exportButton.addActionListener(e -> {
            if (onExport != null) {
                onExport.run();
            }
        });

        // 添加到面板
        add(new JLabel("搜索："));
        add(searchField);
        add(searchButton);
        add(refreshButton);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(addButton);
        add(editButton);
        add(deleteButton);
        add(exportButton);
    }

    public void setOnSearch(Runnable onSearch) {
        this.onSearch = onSearch;
    }

    public void setOnRefresh(Runnable onRefresh) {
        this.onRefresh = onRefresh;
    }

    public void setOnAdd(Runnable onAdd) {
        this.onAdd = onAdd;
    }

    public void setOnEdit(Runnable onEdit) {
        this.onEdit = onEdit;
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnExport(Runnable onExport) {
        this.onExport = onExport;
    }

    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    public void setSearchKeyword(String keyword) {
        searchField.setText(keyword);
    }

    public void clearSearch() {
        searchField.setText("");
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}
