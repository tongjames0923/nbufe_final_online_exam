<template>
    <div>
        <el-dialog title="提示" :visible.sync="this.isshow" width="30%">
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
import { add } from '@/api/tag';

export default {
    name: "TagAdd",
    data() {
        return {
            value: "",
            isshow: false,
            aftershow:undefined
        }
    },
    methods: {
        show(action)
        {
            this.isshow=true;
            this.aftershow=action;
        },
        submit() {
            add(this.value).then(res=>{
                this.$message({
                        message: '增加标签成功',
                        type: 'success'
                    });
            }).finally(()=>{this.cancel()
            
            if(this.aftershow!=undefined)
            {
                this.aftershow();
            }
            
            });
        },
        cancel() {
            this.value = ""
            this.isshow = false

        }
    }
};
</script>