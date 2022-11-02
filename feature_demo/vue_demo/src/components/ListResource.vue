<template>
  <el-main>
    <el-header>
      <el-radio v-model="tp" @change="selectChange" label="1000">文本</el-radio>
      <el-radio v-model="tp" @change="selectChange" label="2000">图片</el-radio>
      <el-radio v-model="tp" @change="selectChange" label="3000">视频</el-radio>
      <el-radio v-model="tp" @change="selectChange" label="4000">音频</el-radio>
    </el-header>
    <ul class="infinite-list"
    infinite-scroll-disabled="disabled"
    v-infinite-scroll="load" style="overflow: auto">
      <li
        v-for="(item, index) in resList"
        class="infinite-list-item"
        :key="index"
      >
        <el-descriptions title="资源信息">
          <el-descriptions-item label="资源类型">{{
            arr[item.resource_type / 1000]
          }}</el-descriptions-item>
          <el-descriptions-item label="资源备注">{{
            item.note
          }}</el-descriptions-item>
          <el-descriptions-item label="最近修改时间">{{
            item.altertime
          }}</el-descriptions-item>
          <el-descriptions-item label="资源链接">
            <el-link
              type="success"
              :href="'http://localhost:8080' + item.resource"
              target="_blank"
              >点击打开</el-link
            >
          </el-descriptions-item>
          <el-descriptions-item v-if="tp == 2000" label="资源预览">
            <el-avatar
              shape="fill"
              :size="300"
              :fit="fit"
              :src="'http://localhost:8080' + item.resource"
            ></el-avatar>
          </el-descriptions-item>
        </el-descriptions>

      </li>
    </ul>
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
    getinfo(type) {
      var that = this;
      this.$getMethod(
        this.url + "?type=" + type + "&from=" + this.from + "&num=" + this.num
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
  },
};
</script>