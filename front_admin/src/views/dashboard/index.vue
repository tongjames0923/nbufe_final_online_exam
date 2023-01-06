<template>
    <div style="padding:10px">
        <el-input style="margin-bottom: 6px;"  v-model="info.name">
    <template slot="prepend"> 用 户 名 </template>
</el-input>
<el-input style="margin-bottom: 6px;"  v-model="info.phone" type="tel">
    <template slot="prepend"> 手 机 号 </template>
</el-input>
  <el-input style="margin-bottom: 6px;" type="email" v-model="info.email">
    <template slot="prepend">电子邮件</template>
  </el-input>
  <el-input style="margin-bottom: 6px;"  v-model="info.address">
    <template slot="prepend">联系地址</template>
</el-input>
<el-input style="margin-bottom: 6px;"  v-model="info.note">
    <template slot="prepend">  备 注  </template>
</el-input>
<el-input style="margin-bottom: 6px;" v-model="levelText[info.level+1]"  disabled>
    <template slot="prepend">  级 别  </template>
</el-input>
<el-button @click="updateInfo">更新信息</el-button>
<el-button @click="resetthis">恢复个人信息</el-button>
    </div>
  <!-- <el-descriptions title="用户信息">
    <el-descriptions-item label="用户名">{{this.info==undefined?'空':this.info.name}}</el-descriptions-item>
    <el-descriptions-item label="手机号">{{this.info==undefined?'空':this.info.phone}}</el-descriptions-item>
    <el-descriptions-item label="电子邮箱">{{this.info==undefined?'空':this.info.email}}</el-descriptions-item>
    <el-descriptions-item label="联系地址">{{this.info==undefined?'空':this.info.address}}</el-descriptions-item>
    <el-descriptions-item label="备注">{{this.info==undefined?'空':this.info.note}}</el-descriptions-item>
</el-descriptions> -->
</template>

<script>
import { getToken, setToken } from '@/utils/auth';
import { setDetail } from '@/api/user';
/* eslint-disable */
export default {
    name:'UserInfo',
    data(){
        return{
            info:undefined,
            levelText:["未激活","标准激活","资源管理员","高级管理员"],
        };
    },
    methods:{
        put(user)
        {
            this.info=user;
            this.$forceUpdate();
        },
        updateInfo()
        {
            setDetail(this.info).then(res=>{
                this.$message({
                        message: '更新成功',
                        type: 'success'
                    });
                    this.$store.commit('SET_TOKEN', res)
                    setToken(res)
                    this.put(res);
            })
        },
        resetthis()
        {
            this.info=JSON.parse(getToken());
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