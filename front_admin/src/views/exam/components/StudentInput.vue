<template>
  <div>
    <el-upload
      v-loading="!selected"
      ref="upload"
      :http-request="uploadfile"
      :auto-upload="true"
      :multiple="false"
      :limit="1"
      action="#"
      :file-list="files"
    >
      <el-button size="small" type="primary">点击上传学生名单Excel</el-button>
      <div slot="tip" class="el-upload__tip">只能上传excel文件</div>
    </el-upload>
    <el-row :gutter="20">
  <el-col :span="6"> <el-select v-model="col[0]" placeholder="考生身份证列选择">
      <el-option
        v-for="item in options"
        :key="item.index"
        :label="item.item"
        :value="item.index"
      >
      </el-option>
    </el-select></el-col>
  <el-col :span="6">    <el-select v-model="col[1]" placeholder="考生姓名列选择">
      <el-option
        v-for="item in options"
        :key="item.index"
        :label="item.item"
        :value="item.index"
      >
      </el-option>
    </el-select></el-col>
  <el-col :span="6">    <el-select v-model="col[2]" placeholder="考生考号列选择">
      <el-option
        v-for="item in options"
        :key="item.index"
        :label="item.item"
        :value="item.index"
      >
      </el-option>
    </el-select></el-col>
</el-row>
    <el-button @click="readstudents()">执行读取</el-button>
    <el-collapse v-model="showlist">
      <el-collapse-item title="考生列表" name="list">
        <el-table :data="students" style="width: 100%">
          <el-table-column prop="id" label="身份证号" width="180">
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="180">
          </el-table-column>
          <el-table-column prop="number" label="学号"> </el-table-column>
        </el-table>
      </el-collapse-item>
    </el-collapse>
    <el-button @click="ok">提交</el-button>
  </div>
</template>

<script>
const confirmFuc=function(data){}
import { readEXCEL,getRowHead, excel_each } from "@/utils/excel";
export default {
  props:{
    action:confirmFuc
  },
  data() {
    return {
      files: [],
      selected: true,
      col: [undefined, undefined, undefined],
      options: [],
      sheet: undefined,
      students: [],
      showlist: ["list"],
    };
  },
  methods: {
    ok()
    {
      this.action(this.students);
    },
    readstudents() {
      this.students.length = 0;
      var that =this
      excel_each(this.sheet,function(vp){
        
        that.students.push({ id: vp[that.col[0]], name:vp[that.col[1]], number: vp[that.col[2]] });
      })

    },
    resetAll() {
      this.sheet = undefined;
      this.options = [];
      this.file = [];
      this.col = [undefined, undefined, undefined];
    },
    uploadfile(fileobject) {
      this.selected = false;
      this.resetAll();
      let file = fileobject.file;
      readEXCEL(file,0)
        .then((workSheet) => {
          this.sheet = workSheet;
          this.options = getRowHead(workSheet);
        })
        .catch((error) => {
          this.$message({
            message: "打开excel表失败，"+error,
            type: "error",
          });
        })
        .finally(() => {
          this.selected = true;
        });
    },
  },
};
</script>

<style>
</style>