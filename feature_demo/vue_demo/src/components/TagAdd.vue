<template>
    <div>
        <el-dialog title="提示" :visible.sync="this.isshow" width="30%" :before-close="handleClose">
        <el-input placeholder="输入Tag名称" v-model="value"></el-input>
        <span slot="footer" class="dialog-footer">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submit">提 交</el-button>
        </span>
    </el-dialog>
    </div>

</template>
  
<script>
/* eslint-disable */
import axios from 'axios';
export default {
    name: "TagAdd",
    data() {
        return {
            value: "",
            isshow: false
        }
    },
    methods: {
        show()
        {
            this.isshow=true;
        },
        submit() {
            axios({
                method: "get",
                url: this.$baseUrl + "tag/add",
                params: {
                    tag: this.value
                }
            }).then(res => {
                if (res.data.code == this.$code_success) {
                    this.$message({
                        message: '增加标签成功',
                        type: 'success'
                    });
                }
                else {
                    this.$message.error('新增标签失败');
                }
            }).finally(() => {
                this.cancel();
            });
        },
        cancel() {
            this.value = ""
            this.isshow = false

        }
    }
};
</script>