<template>
  <div>
    <el-steps :active=steps simple>
      <el-step title="提交必要信息" icon="el-icon-info"></el-step>
      <el-step title="回答密保问题" icon="el-icon-question"></el-step>
      <el-step title="修改密码" icon="el-icon-question"></el-step>
    </el-steps>

    <div v-if="steps === 1">
      <el-input placeholder="请输入您账户的用户名" v-model="username"></el-input>
    </div>
    <div v-else-if="steps === 2">
      <el-input v-model="que_text" disabled>
        <template slot="prepend">密保问题</template>
      </el-input>
      <el-input v-model="que_ans">
        <template slot="prepend">密保答案</template>
      </el-input>
    </div>
    <div v-else-if="steps === 3">
      <el-input v-model="newpassword" placeholder="请输入您的新密码">
        <template slot="prepend">新密码</template>
      </el-input>
    </div>
    <div v-else>

    </div>
    <el-button @click="nextStep()">{{ btn_text }}</el-button>
  </div>
</template>

<script>
import { changePasswordBySec, getQues, replyAns } from '@/api/user';
export default {
  name: "UnLogined",
  data() {
    return {
      steps: 1,
      btn_text: "下一步",
      username: '',
      que_text: '',
      que_ans: '',
      newpassword: ''
    };
  },
  methods: {
    nextStep() {
      if (this.steps === 1) {
        getQues(this.username).then(res => {
          this.que_text = res
          this.steps++;
        })
      } else if (this.steps === 2) {
        replyAns(this.username, this.que_ans).then(res => {
          this.steps++;
        })
      }
      else if (this.steps === 3) {
        changePasswordBySec(this.username, this.newpassword, this.que_ans).then(res => {
          this.steps++;
          this.$message({
            message: '修改密码成功',
            type: 'success'
          });
          this.$router.push({ path: '/' })
        })
      }
    }
  }
}
</script>

<style scoped>

</style>