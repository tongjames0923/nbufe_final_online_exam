<template>
  <el-form ref="userdata" :model="userdata" :rules="rul">
    <el-form-item label="用户名" prop="username">
      <el-input v-model="userdata.username" placeholder="您的用户名" />
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input
        v-model="userdata.password"
        placeholder="请输入您的密码"
        show-password
      />
    </el-form-item>
    <el-form-item label="密保问题">
      <el-input
        v-model="userdata.question"
        placeholder="请输入您的密保问题"
      />
    </el-form-item>
    <el-form-item label="密保答案">
      <el-input
        v-model="userdata.answer"
        placeholder="请输入您的密保答案"
      />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input
        v-model="userdata.email"
        placeholder="请输入您的邮箱"
      />
    </el-form-item>
    <el-form-item label="地址">
      <el-input
        v-model="userdata.address"
        placeholder="请输入您的地址"
      />
    </el-form-item>
    <el-form-item label="个性签名">
      <el-input
        v-model="userdata.note"
        placeholder="请输入您的个性签名"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="toReg()">注册</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
/* eslint-disable */
import {register} from '@/api/user'
import route from '@/router/index'
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
            
            register(this.$qs.stringify(this.userdata))
              .then(function (res) {
                  that.$notify({
                    title: "注册成功",
                    message: res.message,
                    duration: 0,
                    type: "success",
                  });
                  route.push({ path:'/' })
              }).catch(error=>{
                console.log(error)
              });
            return true;
          } else {
            return false;
          }
        });
      },
    }
  };
  </script>