<template>
  <el-main>
    <el-row>
      <el-switch
        v-model="smode"
        active-text="按类型搜索"
        inactive-text="按备注搜索"
      >
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
      <el-button @click="search" icon="el-icon-search" class="blankDiv"
        >搜索</el-button
      >
    </el-header>
    <el-table :data="resList" border>
      <el-table-column label="预览" width="120" fixed>
        <template slot-scope="scope">
          <el-avatar
            v-if="scope.row.resource_type == 2000"
            shape="fill"
            :size="100"
            :src="'http://localhost:8080' + scope.row.resource"
          ></el-avatar>
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
      <el-table-column label="备注" width="750" prop="note"> </el-table-column>
      <el-table-column label="链接" width="120" fixed>
        <template slot-scope="scope">
          <el-link
            type="success"
            :href="'http://localhost:8080' + scope.row.resource"
            target="_blank"
            >点击打开</el-link
          >
        </template>
      </el-table-column>
      <el-table-column label="删除" fixed>
        <template slot-scope="scope">
          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
            @click="deleteItem(scope.$index)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-main>
</template>
  
  <script>
/* eslint-disable */
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
    };
  },

  methods: {
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
      this.$getMethod(
        this.url +
          "/getByNote?note=" +
          this.input +
          "&from=" +
          this.from +
          "&num=" +
          this.num
      ).then((res) => {
        if (res.data.code == 40000) {
          for (let i = 0; i < res.data.data.length; i++) {
            that.resList.push(res.data.data[i]);
          }
          that.$forceUpdate();
        }
      });
    },
    deleteItem(rid) {
      var that = this;
      debugger;
      this.$getMethod(
        this.url +
          "/delete?userid=" +
          this.user +
          "&resource_id=" +
          this.resList[rid].id
      ).then((res) => {
        if (res.data.code == 40000) {
          that.$notify({
            title: "删除成功",
            message: "",
            duration: 1000,
          });
          this.resList.splice(rid, 1);
          that.$forceUpdate();
        } else {
          that.$notify({
            title: "删除失败",
            message: res.data.message,
            duration: 0,
          });
        }
      });
    },
    getinfo(type) {
      var that = this;
      this.$getMethod(
        this.url +
          "/getByType?type=" +
          type +
          "&from=" +
          this.from +
          "&num=" +
          this.num
      ).then(function (res) {
        if (res.data.code == 40000) {
          for (let i = 0; i < res.data.data.length; i++) {
            that.resList.push(res.data.data[i]);
          }
          that.$forceUpdate();
        }
      });
    },
  },
  mounted() {
    console.log("url:" + this.url);
    this.getinfo(2000);
  },
  props: {
    url: String,
    user: Number,
  },
};
</script>
<style>
.blankDiv {
  margin-top: 10px;
}
</style>