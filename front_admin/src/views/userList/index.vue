<template>
<div>
<!--  <el-descriptions title="用户信息">-->
<!--    <el-descriptions-item label="用户名">{{this.info==undefined?'空':this.info.name}}</el-descriptions-item>-->
<!--    <el-descriptions-item label="手机号">{{this.info==undefined?'空':this.info.phone}}</el-descriptions-item>-->
<!--    <el-descriptions-item label="电子邮箱">{{this.info==undefined?'空':this.info.email}}</el-descriptions-item>-->
<!--    <el-descriptions-item label="联系地址">{{this.info==undefined?'空':this.info.address}}</el-descriptions-item>-->
<!--    <el-descriptions-item label="备注">{{this.info==undefined?'空':this.info.note}}</el-descriptions-item>-->
<!--  </el-descriptions>-->
<div v-if="authable" >
  <el-table
      :data="tableData"
      height="980"
      border
      style="width: 100%">
    <el-table-column
        prop="name"
        label="姓名"
        width="180">
    </el-table-column>
    <el-table-column
        prop="phone"
        label="电话号码"
        width="180">
    </el-table-column>
    <el-table-column
        prop="address"
        label="地址">
    </el-table-column>
    <el-table-column
        prop="note"
        label="备注">
    </el-table-column>
    <el-table-column
        label="级别"
        width="500"
        fixed
        >
        <template slot-scope="data">
          <el-radio-group v-model="data.row.level" size="mini"
          @input="changeLevel(data.row)"
          >
      <el-radio-button label=-1>未激活</el-radio-button>
      <el-radio-button label=0>标准激活用户</el-radio-button>
      <el-radio-button label=1>资源写入权限用户</el-radio-button>
      <el-radio-button label=2>高级管理员</el-radio-button>
    </el-radio-group>
        </template>
    </el-table-column>
  </el-table>
  <el-pagination @current-change="handleCurrentChange" :current-page.sync="cur" :page-size="per"
            layout="total, prev, pager, next" :total=total>
        </el-pagination>
</div>
  <div v-else>您没有权限操作其他用户</div>
</div>
</template>

<script>
import { getToken } from '@/utils/auth';
import { allUserCount, pullUserList, updateLevel } from '@/api/user';
export default {
  name: "UserList",
  data(){
    return{
      tableData:[],
      authable:false,
      total: -1,
                cur: 0,
                per: 30,
                user:undefined
    };
  },
  methods:{
    updateTotal()
    {
      allUserCount().then(res=>{
        this.total=res
      })
    },
    updateList(id,from,num)
    {
      pullUserList(id,from,num).then(res=>{
        this.tableData=res;
        this.$forceUpdate();
      })
      this.updateTotal();
    },
    changeLevel(data)
    {
      console.log(data)
      updateLevel(this.user.id,data.id,data.level).then(res=>{
        
      })
    }
  },
  mounted(){
    this.user=JSON.parse(getToken());

    this.authable=this.user.level===2
    if(this.authable)
    {
      this.updateList(this.user.id,this.cur,this.per)
    }
  },
}
</script>

<style scoped>

</style>