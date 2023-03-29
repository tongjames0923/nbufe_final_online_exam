<template>
  <el-main>
    <el-row>
      <el-switch v-model="smode" active-text="按类型搜索" inactive-text="按备注搜索">
      </el-switch>
    </el-row>

    <el-divider></el-divider>
    <el-header v-if="smode">
      <el-radio v-model="tp" @change="selectChange" label="1000">文本</el-radio>
      <el-radio v-model="tp" @change="selectChange" label="2000">图片</el-radio>
      <el-radio v-model="tp" @change="selectChange" label="3000">视频</el-radio>
      <el-radio v-model="tp" @change="selectChange" label="4000">音频</el-radio>
    </el-header>
    <el-header v-else>
      <el-input v-model="input" placeholder="请输入需要搜索的备注"></el-input>
      <el-button @click="search" icon="el-icon-search" class="blankDiv">搜索</el-button>
    </el-header>
    <div style="height:30px"></div>
    <el-table :data="resList" border>
      <el-table-column label="预览" width="120" fixed>
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.resource_type == 2000" shape="fill" :size="100"
            :src="SerLocation + scope.row.resource"></el-avatar>
          <el-empty v-else></el-empty>
        </template>
      </el-table-column>
      <el-table-column fixed prop="altertime" label="日期" width="250">
      </el-table-column>
      <el-table-column fixed label="资源类型" width="100">
        <!--  -->
        <template slot-scope="scope">
          <el-tag>{{ arr[scope.row.resource_type / 1000] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" width="750">
        <template slot-scope="data">
          <el-input placeholder="备注"
            @blur="changeResource(data.row.id,data.row.note)" v-model="data.row.note"
            style="width: auto;"
            >
          </el-input>
        </template>

      </el-table-column>
      <el-table-column label="链接" width="80" fixed>
        <template slot-scope="scope">
          <el-popover placement="top" trigger="click">
            <div style="text-align: left;; margin: 0">
              <el-link type="success" :href="SerLocation + scope.row.resource" target="_blank">
                链接:{{ SerLocation + scope.row.resource }}</el-link>
              <el-button style="margin-left: 12px;" type="text" size="medium"
                @click="copy(SerLocation + scope.row.resource); ">拷贝链接</el-button>
            </div>
            <el-button type="text" size="medium" slot="reference">查看</el-button>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="删除" fixed>
        <template slot-scope="scope">
          <el-button type="danger" icon="el-icon-delete" circle @click="deleteItem(scope.$index)"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-main>
</template>
  
<script>
/* eslint-disable */


import { api_changeResourceNote, deleteResource, getInfoByType, searchByNote } from '@/api/resource'
import { Server } from '@/settings';
export default {
  name: "ListResourceVue",

  data() {
    return {
      resList: [],
      from: 0,
      num: 1000,
      tp: 2000,
      arr: ["请选择文件类型", "文本", "图片", "视频", "音频"],
      input: "",
      smode: true,
      SerLocation: undefined
    };
  },

  methods: {
    copy(text) {
      this.$copyText(text).then(success => {
        this.$message({
          type: 'success',
          message: "拷贝成功"
        })
      },
        err => {
          this.$message({
            type: 'error',
            message: "拷贝失败"
          })
        })
    },
    changeResource(id,note)
    {
      this.$confirm(
          "确认将[" + id + "]资源的备注改成["+note+"]吗？",
          "确认修改资源备注",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).then(() => {
          api_changeResourceNote(id,note).then(()=>{
            that.$notify({
            title: "修改成功",
            message: "",
            duration: 800,
          });
          }).catch(e=>{
            this.getinfo(this.tp);
          });
        }).catch(e=>{
          this.getinfo(this.tp);
        })
    },
    load() {
      console.log("loading");
      this.getinfo(this.tp);
    },
    selectChange() {
      this.from = 0;
      this.getinfo(this.tp);
      this.resList = [];
    },
    search() {
      var that = this;
      this.resList = [];
      searchByNote(this.input, this.from, this.num)
        .then((res) => {

          for (let i = 0; i < res.length; i++) {
            that.resList.push(res[i]);
          }
          that.$forceUpdate();
        });
    },
    deleteItem(rid) {
      var that = this;
      deleteResource(this.resList[rid].id).then((res) => {
        {
          that.$notify({
            title: "删除成功",
            message: "",
            duration: 1000,
          });
          this.resList.splice(rid, 1);
          that.$forceUpdate();
        }
      });
    },
    getinfo(type) {
      var that = this;
      this.resList=[]
      getInfoByType(type, this.from, this.num).then(function (res) {
        for (let i = 0; i < res.length; i++) {
          that.resList.push(res[i]);
        }
        that.$forceUpdate();
      });
    },
  },
  beforeMount() {
    this.getinfo(2000);
    this.SerLocation = Server
  },
};
</script>
<style>
.blankDiv {
  margin-top: 10px;
}
</style>