package com.kyle.kyleaiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Haoran Wang
 * @date 2025/6/15 20:50
 */

@EqualsAndHashCode(callSuper=true)
@Data
public abstract class ReActAgent extends BaseAgent {
    /**
     * 处理当前状态 并决定是否要进行下一步行动(act)
     * @return 是否需要执行行动，T/F ===> 执行/不执行
     */
    public abstract boolean think();

    /**
     * 执行think决定要执行的行为
     * @return 执行结果
     */
    public abstract String act();

    /**
     * 将step方法 细分为两个方法 think and act
     * @return
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if(!shouldAct){
                return "Thinking completed - no action needed";
            }
            return act();
        } catch (Exception e) {
            e.printStackTrace();
            return "步骤执行失败" + e.getMessage();
        }
    }
}
