<template>
  <el-form :model="login" :rules="rules" ref="login">
    <el-form-item label="用户名" prop="username">
      <el-input placeholder="您的用户名" v-model="login.username"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input
        placeholder="请输入您的密码"
        v-model="login.password"
        show-password
      ></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="tologin()">登录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
/* eslint-disable */
export default {
  name: "Login",

  data() {
    return {
      rules: {
        username: [
            {required: true,message:"请输入您的用户名",trigger:'blur'},
          {
            min: 6,
            max: 16,
            message: "用户名长度在 6 到 16 个字符",
            trigger: "change"
          },
        ],
        password: [
        {required: true,message:"请输入您的密码",trigger:'blur'},
          {
            required:true,
            min: 8,
            max: 18,
            message: "密码长度在 8 到 18 个字符",
            trigger: "change"
          },
        ],
      },

      login: {
        username: "",
        password: "",
      },
    };
  },

  methods: {
    tologin() {
      var that = this;
      this.allowed(this,"login",function(){
        that.$getMethod(
              that.url +
                "?username=" +
                that.login.username +
                "&password=" +
                that.login.password
            )
            .then(function (res) {
              if (res.data.code == 40000) {
                that.$notify({
                  title: "登录成功",
                  message: res.data.message,
                  duration: 0,
                  type: "success",
                });
              } else {
                that.$notify({
                  title: "登录失败",
                  message: res.data.message,
                  duration: 0,
                  type: "error",
                });
              }
            });
      },function(){
        console.log("error submit!!");
      });
    },
  },
  created() {
    console.log("url:" + this.url);
  },
  props: {
    url: String,
  },
};
</script>