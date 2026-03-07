<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
      <div class="logo">
        <span v-if="!isCollapse">学生信息管理系统</span>
        <span v-else>学管</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :collapse="isCollapse"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>概览</span>
        </el-menu-item>
        <el-menu-item index="/students">
          <el-icon><User /></el-icon>
          <span>学生管理</span>
        </el-menu-item>
        <el-menu-item index="/teachers">
          <el-icon><UserFilled /></el-icon>
          <span>教师管理</span>
        </el-menu-item>
        <el-menu-item index="/courses">
          <el-icon><Reading /></el-icon>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="/scores">
          <el-icon><TrendCharts /></el-icon>
          <span>成绩管理</span>
        </el-menu-item>
        <el-menu-item index="/attendance">
          <el-icon><Calendar /></el-icon>
          <span>考勤管理</span>
        </el-menu-item>
        <el-menu-item index="/enrollments">
          <el-icon><Switch /></el-icon>
          <span>选课管理</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><PieChart /></el-icon>
          <span>统计分析</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-icon" @click="toggleCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" icon="User" />
              <span class="username">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人设置</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { 
  DataAnalysis, User, UserFilled, Reading, 
  TrendCharts, Calendar, Switch, PieChart,
  Fold, Expand
} from '@element-plus/icons-vue'

const route = useRoute()
const isCollapse = ref(false)

const activeMenu = computed(() => route.path)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100%;
}

.sidebar {
  background-color: #304156;
  transition: width 0.3s;
  
  .logo {
    height: 60px;
    line-height: 60px;
    text-align: center;
    color: #fff;
    font-size: 18px;
    font-weight: bold;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  
  .header-left {
    .collapse-icon {
      font-size: 20px;
      cursor: pointer;
      transition: color 0.3s;
      
      &:hover {
        color: #409EFF;
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      
      .username {
        margin-left: 10px;
        color: #606266;
      }
    }
  }
}

.main-content {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>
