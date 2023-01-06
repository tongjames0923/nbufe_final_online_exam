<template>
  <div>
    <el-input placeholder="请输入原密码" v-model="old"></el-input>
    <el-input placeholder="请输入新密码" type="password" v-model="newone" show-password></el-input>
    <el-popover placement="top" width="160" v-model="visible">
      <p>确认修改密码？</p>
      <div style="text-align: right; margin: 0">
        <el-button size="mini" type="text" @click="visible = false">取消</el-button>
        <el-button type="primary" size="mini" @click="visible = false;submit()">确定</el-button>
      </div>
      <el-button slot="reference">修改</el-button>
    </el-popover>
  </div>

</template>

<script>
import { getToken } from '@/utils/auth';
import { changePasswordByOld } from '@/api/user';
export default {
  name: "Logined",
  data() {
    return {
      old: '',
      newone: '',
      visible:false
    };
  },
  methods: {
    submit() {
      let obj = JSON.parse(getToken());
      changePasswordByOld(obj.id, this.old, this.newone).then(res => {
        this.$message({
          message: '修改密码成功',
          type: 'success'
        });
      })
    }
  }
}
</script>

<style scoped>

</style>