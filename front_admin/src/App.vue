<template>
  <div id="app">
    <router-view style="padding: 15px;" />
  </div>
</template>

<script>
import { mount } from '@vue/test-utils'
import { api_renew_access } from './api/user';
export default {
  name: 'App',
  mounted() {
    let timer = setInterval(() => {
      api_renew_access().then(res => {

      });
      console.log("renewing");
    }, 60 * 1000)
  }
}
</script>
