<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <UploadFile></UploadFile>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import UploadFile from './components/UploadFile.vue'

export default {
  name: 'App',
  components: {
    // HelloWorld,
    UploadFile
  }
}
</script>

<style>
#app {
  font-family: comic code, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align:center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
