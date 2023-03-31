/**
 * Copyright 2023 json.cn
 */
package tbs.api_server.ai.model;

/**
 * Auto-generated: 2023-03-31 10:11:10
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Usage {

    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;

    @Override
    public String toString() {
        return "Usage{" +
                "prompt_tokens=" + prompt_tokens +
                ", completion_tokens=" + completion_tokens +
                ", total_tokens=" + total_tokens +
                '}';
    }

    public void setPrompt_tokens(int prompt_tokens) {
        this.prompt_tokens = prompt_tokens;
    }
    public int getPrompt_tokens() {
        return prompt_tokens;
    }

    public void setCompletion_tokens(int completion_tokens) {
        this.completion_tokens = completion_tokens;
    }
    public int getCompletion_tokens() {
        return completion_tokens;
    }

    public void setTotal_tokens(int total_tokens) {
        this.total_tokens = total_tokens;
    }
    public int getTotal_tokens() {
        return total_tokens;
    }

}