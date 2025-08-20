import axios from 'axios';
import { createSSEConnection } from '../utils/sse';

// const API_URL = 'https://kyle-agent-backend-168280-7-1307438698.sh.run.tcloudbase.com/api/ai';
// 根据环境变量设置 API 基础 URL
const API_URL = process.env.NODE_ENV === 'production' 
 ? 'https://kyle-agent-backend-168280-7-1307438698.sh.run.tcloudbase.com/api/ai' // 生产环境使用相对路径，适用于前后端部署在同一域名下
 : 'http://localhost:8123/api/ai' // 开发环境指向本地后端服务

/**
 * AI社交大师聊天API
 * @param {string} message - 用户消息
 * @param {string} chatId - 会话ID
 * @param {Function} onMessage - 消息处理函数
 * @param {Function} onError - 错误处理函数
 * @param {Function} onComplete - 完成处理函数
 * @returns {EventSource} SSE连接
 */
export function chatWithSocialMaster(message, chatId, onMessage, onError, onComplete) {
  const url = `${API_URL}/social_app/chat/sse_emitter?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`;
  return createSSEConnection(url, onMessage, onError, onComplete);
}

/**
 * AI超级智能体聊天API
 * @param {string} message - 用户消息
 * @param {Function} onMessage - 消息处理函数
 * @param {Function} onError - 错误处理函数
 * @param {Function} onComplete - 完成处理函数
 * @returns {EventSource} SSE连接
 */
export function chatWithSuperAgent(message, onMessage, onError, onComplete) {
  const url = `${API_URL}/manus/chat?message=${encodeURIComponent(message)}`;
  return createSSEConnection(url, onMessage, onError, onComplete);
} 