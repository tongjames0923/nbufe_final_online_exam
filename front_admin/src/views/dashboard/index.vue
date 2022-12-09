<template>
  <el-descriptions title="用户信息">
    <el-descriptions-item label="用户名">{{this.info==undefined?'空':this.info.name}}</el-descriptions-item>
    <el-descriptions-item label="手机号">{{this.info==undefined?'空':this.info.phone}}</el-descriptions-item>
    <el-descriptions-item label="电子邮箱">{{this.info==undefined?'空':this.info.email}}</el-descriptions-item>
    <el-descriptions-item label="联系地址">{{this.info==undefined?'空':this.info.address}}</el-descriptions-item>
    <el-descriptions-item label="备注">{{this.info==undefined?'空':this.info.note}}</el-descriptions-item>
</el-descriptions>
</template>

<script>
import { getToken } from '@/utils/auth';
/* eslint-disable */
export default {
    name:'UserInfo',
    data(){
        return{
            info:undefined,
        };
    },
    methods:{
        put(user)
        {
            this.info=user;
            this.$forceUpdate();
        }
    },
    mounted()
    {
      let obj=JSON.parse(getToken());
      this.info=obj;
    }
}
</script>

<style>

</style>