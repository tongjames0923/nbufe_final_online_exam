
<template>
  <el-form
    :model="ruleForm"
    :rules="rules"
    ref="ruleForm"
    :v-loading="isloading"
    :on-submit="submit"

  >
    <el-form-item label="资源类型" prop="type" required>
      <el-radio v-model="ruleForm.type" @change="selectChange" label="1000"
        >文本</el-radio
      >
      <el-radio v-model="ruleForm.type" @change="selectChange" label="2000"
        >图片</el-radio
      >
      <el-radio v-model="ruleForm.type" @change="selectChange" label="3000"
        >视频</el-radio
      >
      <el-radio v-model="ruleForm.type" @change="selectChange" label="4000"
        >音频</el-radio
      >
    </el-form-item>
    <el-form-item label="备注" prop="note">
      <el-input v-model="ruleForm.note" placeholder="请输入内容"></el-input>
    </el-form-item>
    <el-form-item label="文件" required>
      <el-upload
        v-loading="!selected"
        ref="upload"
        :http-request="uploadfile"
        :auto-upload="false"
        :multiple="false"
        :limit="1"
        :on-change="submitUpload"
        action="#"
        :accept="acp"
        :file-list="files"
      >
      <el-button slot="trigger" size="small" type="primary"> {{ tip }}</el-button>
      </el-upload>
    </el-form-item>
    <el-button type="primary" @click="submit(ruleForm)">提交</el-button>
    <el-button type="cancel" @click="cancel">取消提交</el-button>
  </el-form>
</template>

<script>
/* eslint-disable */
import axios from "axios";
export default {
  name: "UploadFile",
  data() {
    return {
      isloading: false,
      ruleForm: {
        note: "",
        type: 1000,
        file: null,
      },
      files:[],
      hasFile:false
      ,
      selected: false,
      acp: "none",
      tips_arr: [
        "请选择文件类型",
        "请选择文本文件",
        "请选择图片文件",
        "请选择视频文件",
        "请选择音频文件",
      ],
      tip: "请选择文件类型",
      acp_arr: ["text/*", "image/*", "video/*", "audio/*"],
      rules: {
        type: [{ required: true, message: "请选择类型", trigger: "blur" }],
        note: [{ max: 128, message: "最长128个字符", trigger: "blur" }],
      },
    };
  },
  props: {
    url: String,
  },
  mounted() {
    console.log("url is :" + this.url);
  },
  methods: {
    uploadfile(fileobject) {
      var that = this;
      this.isloading = true;
      console.log(fileobject.file);
      var formDataInfo = new FormData();
      formDataInfo.append("file", fileobject.file);
      formDataInfo.append("type", this.ruleForm.type);
      formDataInfo.append("note", this.ruleForm.note);
      console.log(formDataInfo);
      axios.post(this.url, formDataInfo).then((res) => {
        that.isloading = false;
        console.log(res.data);
        that.$notify({
          title: res.data.code==40000 ? "上传成功" : "上传失败",
          message: "",
          duration: 1500,
        });
      });
    },
    selectChange(label) {
      this.selected = true;
      this.acp = this.acp_arr[label / 1000 - 1];
      this.tip = this.tips_arr[label / 1000];
      console.log(this.acp);
    },
    submit() {
      var that = this;
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          if (that.hasFile) {
            that.$refs.upload.submit();
          } else {
            that.$notify({
              title: "错误!",
              message: "请选择要上传的文件",
              duration: 1000,
            });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
      this.hasFile=false;
      this.files=[];
    },
    cancel() {},
    submitUpload(file, fileList) {
      this.ruleForm.file = file.file;
      this.hasFile=true;
    },
  },
};
</script>

<style>
</style>