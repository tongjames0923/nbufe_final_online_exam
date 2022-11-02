<template>
  <el-form :model="userdata" :rules="rul" ref="userdata">
    <el-form-item label="用户名" prop="username">
      <el-input placeholder="您的用户名" v-model="userdata.username"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input
        placeholder="请输入您的密码"
        v-model="userdata.password"
        show-password
      ></el-input>
    </el-form-item>
    <el-form-item label="密保问题">
      <el-input
        placeholder="请输入您的密保问题"
        v-model="userdata.question"
      ></el-input>
    </el-form-item>
    <el-form-item label="密保答案">
      <el-input
        placeholder="请输入您的密保答案"
        v-model="userdata.answer"
      ></el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input
        placeholder="请输入您的邮箱"
        v-model="userdata.email"
      ></el-input>
    </el-form-item>
    <el-form-item label="地址">
      <el-input
        placeholder="请输入您的地址"
        v-model="userdata.address"
      ></el-input>
    </el-form-item>
    <el-form-item label="个性签名">
      <el-input
        placeholder="请输入您的个性签名"
        v-model="userdata.note"
      ></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="toReg()">注册</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
/* eslint-disable */
import axios from "axios";
export default {
  name: "Register",

  data() {
    return {
      rul: {
        username: [
          { required: true, message: "请输入您的用户名", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "用户名长度在 6 到 16 个字符",
            trigger: "change",
          },
        ],
        password: [
          { required: true, message: "请输入您的密码", trigger: "blur" },
          {
            required: true,
            min: 8,
            max: 18,
            message: "密码长度在 8 到 18 个字符",
            trigger: "change",
          },
        ],
        email: [
          {
            type: "email",
            message: "输入电子邮件的格式错误",
            trigger: "blur",
          },
        ],
      },
      userdata: {
        username: "",
        password: "",
        question: null,
        answer: null,
        address: null,
        email: null,
        note: null,
      },
    };
  },

  methods: {
    toReg() {
      this.$refs["userdata"].validate((valid) => {
        if (valid) {
          var that = this;
          axios
            .post(this.url, this.$qs.stringify(this.userdata))
            .then(function (res) {
              if (res.data.code == 40000) {
                that.$notify({
                  title: "注册成功",
                  message: res.data.message,
                  duration: 0,
                  type: "success",
                });
              } else {
                that.$notify({
                  title: "注册失败",
                  message: res.data.message,
                  duration: 0,
                  type: "error",
                });
              }
            });
          return true;
        } else {
          return false;
        }
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