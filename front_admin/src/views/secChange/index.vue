<template>
    <div>
        <el-input placeholder="请输入密保问题" v-model="old"></el-input>
        <el-input placeholder="请输入密保答案" v-model="newone"></el-input>
        <el-popover placement="top" width="160" v-model="visible">
            <p>确认修改密保？</p>
            <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="visible = false">取消</el-button>
                <el-button type="primary" size="mini" @click="visible = false; submit()">确定</el-button>
            </div>
            <el-button slot="reference">修改</el-button>
        </el-popover>
    </div>
</template>
  
<script>
import { getToken } from '@/utils/auth';
import { api_changeSec } from '@/api/user';
export default {
    name: "SecChange",
    data() {
        return {
            old: '',
            newone: '',
            visible: false
        };
    },
    methods: {
        submit() {
            let obj = JSON.parse(getToken());
            api_changeSec(this.old, this.newone).then(res => {
                this.$message({
                    message: '修改密保成功',
                    type: 'success'
                });
            })
        }
    }
}
</script>
  
<style scoped></style>