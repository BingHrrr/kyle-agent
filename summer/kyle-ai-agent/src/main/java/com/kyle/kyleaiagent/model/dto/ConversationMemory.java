package com.kyle.kyleaiagent.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName conversation_memory
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="conversation_memory")
public class ConversationMemory implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     *
     */
    @TableField(value = "conversation_id")
    private String conversationId;

    /**
     * @see org.springframework.ai.chat.messages.MessageType
     */
    @TableField(value = "type")
    private String type;

    /**
     *
     */
    @TableField(value = "memory")
    private String memory;

    /**
     *
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     *
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    /**
     *
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
