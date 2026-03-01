package com.student.view;

import java.awt.*;

/**
 * 统一主题类 - 定义颜色和样式常量
 */
public class Theme {

    // 主色调
    public static final Color PRIMARY_COLOR = new Color(64, 158, 255);      // 蓝色主色
    public static final Color PRIMARY_DARK = new Color(50, 128, 204);       // 深蓝色
    public static final Color PRIMARY_LIGHT = new Color(102, 180, 255);     // 浅蓝色

    // 辅助色
    public static final Color SUCCESS_COLOR = new Color(103, 194, 58);      // 绿色
    public static final Color WARNING_COLOR = new Color(230, 162, 60);      // 橙色
    public static final Color DANGER_COLOR = new Color(245, 108, 108);      // 红色
    public static final Color INFO_COLOR = new Color(144, 197, 237);        // 信息蓝

    // 文本颜色
    public static final Color TEXT_PRIMARY = new Color(48, 49, 51);         // 主要文字
    public static final Color TEXT_SECONDARY = new Color(144, 147, 153);    // 次要文字
    public static final Color TEXT_PLACEHOLDER = new Color(198, 200, 206);  // 占位符文字

    // 背景颜色
    public static final Color BACKGROUND_PRIMARY = new Color(255, 255, 255); // 主背景
    public static final Color BACKGROUND_SECONDARY = new Color(245, 247, 250); // 次要背景
    public static final Color BACKGROUND_DARK = new Color(38, 39, 41);      // 深色背景

    // 边框颜色
    public static final Color BORDER_COLOR = new Color(228, 230, 235);      // 默认边框
    public static final Color BORDER_FOCUS = PRIMARY_COLOR;                 // 聚焦边框

    // 表格颜色
    public static final Color TABLE_HEADER_BG = new Color(245, 247, 250);   // 表头背景
    public static final Color TABLE_ROW_ALT = new Color(250, 252, 255);     // 隔行背景
    public static final Color TABLE_SELECTION = new Color(230, 240, 255);   // 选中行背景
    public static final Color TABLE_GRID = new Color(235, 238, 245);        // 表格线

    // 按钮颜色
    public static final Color BUTTON_DEFAULT_BG = new Color(255, 255, 255);
    public static final Color BUTTON_DEFAULT_BORDER = new Color(209, 213, 219);
    public static final Color BUTTON_PRIMARY_BG = PRIMARY_COLOR;
    public static final Color BUTTON_SUCCESS_BG = SUCCESS_COLOR;
    public static final Color BUTTON_DANGER_BG = DANGER_COLOR;

    // 字体
    public static final String FONT_FAMILY = "Microsoft YaHei";
    public static final Font FONT_DEFAULT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_BOLD = new Font(FONT_FAMILY, Font.BOLD, 12);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, 20);

    // 尺寸
    public static final int BORDER_RADIUS = 4;
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_NORMAL = 10;
    public static final int PADDING_LARGE = 15;

    // 阴影
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);
}
