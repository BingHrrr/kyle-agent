package com.kyle.kyleaiagent.agent.model;


/**
 * @author Haoran Wang
 * @date 2025/6/15 10:46
 */
/**
 * 代理执行状态的枚举类  
 */  
public enum AgentState {  
  
    /**  
     * 空闲状态  
     */  
    IDLE,  
  
    /**  
     * 运行中状态  
     */  
    RUNNING,  
  
    /**  
     * 已完成状态  
     */  
    FINISHED,  
  
    /**  
     * 错误状态  
     */  
    ERROR  
}
