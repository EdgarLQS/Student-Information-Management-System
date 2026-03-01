#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
一键提交脚本 - 快速将变更提交到 GitHub
使用方法：python quick_commit.py [提交信息]
"""

import subprocess
import sys
import re

def run_cmd(cmd):
    """执行命令并返回输出"""
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    return result.returncode, result.stdout, result.stderr

def get_remote_url():
    """获取远程仓库 URL"""
    code, out, _ = run_cmd("git remote get-url origin")
    if code == 0:
        return out.strip()
    return None

def get_commit_url(commit_hash):
    """生成 GitHub 提交链接"""
    remote = get_remote_url()
    if remote:
        match = re.search(r'github\.com[:/](.+?)/(.+?)\.git', remote)
        if match:
            repo = match.group(1) + "/" + match.group(2).replace(".git", "")
            return f"https://github.com/{repo}/commit/{commit_hash}"
    return None

def generate_commit_message(diff_stat):
    """根据变更生成提交信息"""
    files = []
    for line in diff_stat.strip().split('\n'):
        if '|' in line:
            parts = line.split('|')
            if parts:
                filename = parts[0].strip()
                if filename:
                    files.append(filename.split('/')[-1])

    if not files:
        return "feat: 更新代码"

    # 判断变更类型
    type_prefix = "feat"
    if any(kw in diff_stat.lower() for kw in ['fix', 'bug', 'error', 'exception']):
        type_prefix = "fix"
    elif any(kw in diff_stat.lower() for kw in ['refactor', 'clean']):
        type_prefix = "refactor"
    elif any(kw in diff_stat.lower() for kw in ['test', 'spec']):
        type_prefix = "test"
    elif any(kw in diff_stat.lower() for kw in ['doc', 'readme', 'md']):
        type_prefix = "docs"
    elif any(kw in diff_stat.lower() for kw in ['perf', 'optim', 'speed']):
        type_prefix = "perf"

    # 生成描述
    if len(files) == 1:
        desc = f"更新 {files[0]}"
    elif len(files) <= 3:
        desc = f"更新 {', '.join(files)}"
    else:
        desc = f"更新 {len(files)} 个文件"

    return f"{type_prefix}: {desc}"

def main():
    print("=" * 50)
    print("       一键提交脚本")
    print("=" * 50)

    # 检查 git 状态
    print("\n正在检查 git 状态...")
    code, out, err = run_cmd("git status --short")

    if not out.strip():
        print("工作区是干净的，没有需要提交的变更")
        return 0

    print("发现未提交的变更")

    # 显示变更摘要
    print("\n变更摘要:")
    print("-" * 50)
    code, diff_stat, _ = run_cmd("git diff --stat")
    print(diff_stat)

    # 获取提交信息
    commit_msg = sys.argv[1] if len(sys.argv) > 1 else None

    if not commit_msg:
        # 自动生成提交信息
        commit_msg = generate_commit_message(diff_stat)
        print("-" * 50)
        print(f"建议的提交信息：{commit_msg}")
        print("-" * 50)

        # 确认
        confirm = input("是否继续提交？[Y/n]: ").strip().lower()
        if confirm == 'n':
            print("已取消提交")
            return 0

        # 允许修改提交信息
        new_msg = input(f"提交信息 [{commit_msg}]: ").strip()
        if new_msg:
            commit_msg = new_msg
    else:
        confirm = input(f"使用提交信息 '{commit_msg}' 继续？[Y/n]: ").strip().lower()
        if confirm == 'n':
            print("已取消提交")
            return 0

    # 添加变更
    print("\n正在添加变更...")
    code, _, stderr = run_cmd("git add -A")
    if code != 0:
        print(f"添加变更失败：{stderr}")
        return 1
    print("已添加所有变更")

    # 创建提交
    print("正在创建提交...")
    code, _, stderr = run_cmd(f'git commit -m "{commit_msg}"')
    if code != 0:
        print(f"创建提交失败：{stderr}")
        print("提示：如果 pre-commit 钩子失败，请先修复问题后重新运行")
        return 1

    commit_hash = run_cmd("git rev-parse --short HEAD")[1].strip()
    print(f"已创建提交 {commit_hash}: {commit_msg}")

    # 推送到 GitHub
    print("\n正在推送到远程仓库...")
    code, _, stderr = run_cmd("git push")
    if code != 0:
        print(f"推送失败：{stderr}")
        return 1
    print("推送成功")

    # 显示结果
    print("\n" + "=" * 50)
    print("提交成功!")
    print(f"  提交哈希：{commit_hash}")
    print(f"  提交信息：{commit_msg}")

    commit_url = get_commit_url(commit_hash)
    if commit_url:
        print(f"  查看提交：{commit_url}")
    print("=" * 50)

    return 0

if __name__ == "__main__":
    sys.exit(main())
