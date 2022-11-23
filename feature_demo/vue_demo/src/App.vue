<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/> -->

    <el-tabs type="border-card">
      <el-tab-pane label="注册">
        <Register :url="'http://localhost:8080/user/register'"></Register>
      </el-tab-pane>
      <el-tab-pane label="登录">
        <Login :url="'http://localhost:8080/user/login'"></Login>
      </el-tab-pane>
      <el-tab-pane label="上传资源">
        <UploadFile :url="'http://localhost:8080/resource/upload'"></UploadFile>
      </el-tab-pane>
      <el-tab-pane label="查看资源">
        <ListResourceVue :url="'http://localhost:8080/resource'" :user=1></ListResourceVue>
      </el-tab-pane>
      <el-tab-pane label="MarkDown">
        <Question></Question>
      </el-tab-pane>
      <el-tab-pane label="Tabs">
        <question-main ref="ques" ></question-main>
        <el-button @click="change()"></el-button>
      </el-tab-pane>
    </el-tabs>



  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import UploadFile from "./components/UploadFile.vue";
import Login from "./components/Login.vue";
import Register from "./components/Register.vue";
import ListResourceVue from "./components/ListResource.vue";
import Question from './components/question.vue';
import QuestionMain from './components/QuestionMain.vue';
export default {
  name: "App",
  components: {
    UploadFile,
    Login,
    Register,
    ListResourceVue,
    Question,
    QuestionMain,
  },
  data(){
    return{
      id:3
    };
  },
  methods:{
    change()
    {
      this.$refs.ques.updateText(this.id);
      this.id++;
    }
  }
};
</script>

<style>
#app {
  font-family: comic code, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
