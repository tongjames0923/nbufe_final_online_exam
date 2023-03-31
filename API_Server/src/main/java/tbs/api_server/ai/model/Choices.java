/**
 * Copyright 2023 json.cn
 */
package tbs.api_server.ai.model;

/**
 * Auto-generated: 2023-03-31 10:33:26
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Choices {

    private int index;
    private Ai_RequestModel.Message message;
    private String finish_reason;
    public void setIndex(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }

    public void setMessage(Ai_RequestModel.Message message) {
        this.message = message;
    }
    public Ai_RequestModel.Message getMessage() {
        return message;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }
    public String getFinish_reason() {
        return finish_reason;
    }

}