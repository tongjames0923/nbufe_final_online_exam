
<template>
  <el-form
    :model="ruleForm"
    :rules="rules"
    ref="ruleForm"
    label-width="100px"
    class="demo-ruleForm"
    :v-loading=isloading
  >
    <el-form-item label="资源类型" prop="type" required>
      <el-radio v-model="ruleForm.type" label=1000>文本</el-radio>
      <el-radio v-model="ruleForm.type" label=2000>图片</el-radio>
      <el-radio v-model="ruleForm.type" label=3000>视频</el-radio>
      <el-radio v-model="ruleForm.type" label=4000>音频</el-radio>
    </el-form-item>
    <el-form-item label="备注" prop="note">
      <el-input v-model="ruleForm.note" placeholder="请输入内容"></el-input>
    </el-form-item>
    <el-form-item label="文件" prop="file" required>
      <el-upload
        class="upload-demo"
        ref="upload"
        :http-request="uploadfile"
        drag
        :auto-upload=false
        :multiple=false
        :limit=1
        :on-change="submitUpload"
        action="#"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          只能上传jpg/png文件，且不超过500kb
        </div>
      </el-upload>
    </el-form-item>
    <el-button type="primary" @click="submit">提交</el-button>
  </el-form>
</template>

<script>
/* eslint-disable */
import axios from 'axios'
import { MessageBox } from 'element-ui';
export default {
  name: "UploadFile",
  data() {
    return {
        isloading:false,
      ruleForm: {
        note: "",
        type: 1000,
        file: null,
      },
      rules: {
        type: [{ required: true, message: "请选择类型", trigger: "blur" }],
        note: [{ max: 128, message: "最长128个字符", trigger: "blur" }],
        file: [{ required: true, message: "请上传文件", trigger: "change" }],
      },
    };
  },
  methods: {
    uploadfile(fileobject)
    {
        var that=this;
        this.isloading=true;
        console.log(fileobject.file);
        var formDataInfo = new FormData();
        formDataInfo.append("file",fileobject.file);
        formDataInfo.append("type",this.ruleForm.type);
        formDataInfo.append("note",this.ruleForm.note);
        console.log(formDataInfo);
        axios.post('http://localhost:8080/resource/upload',formDataInfo)
    .then(res=>{
        that.isloading=false;
        console.log(res.data);
        that.$notify({
          title: res.data.success?"上传成功":"上传失败",
          message: "upload for "+res.data.data+"bytes",
          duration: 1500
        });

    });
    },
    submit()
    {
        this.$refs.upload.submit();
       
    },

    submitUpload(file, fileList) {
      this.ruleForm.file = file.file;
      console.log(file)
      console.log(this.ruleForm.type);
    },
  },
};
</script>

<style>
</style>