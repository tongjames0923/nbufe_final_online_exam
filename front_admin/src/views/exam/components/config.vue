<template>
  <div>
    <el-form ref="form" model="config">
      <el-form-item label="考试名称" required>
        <el-input v-model="config.exam_name"></el-input>
      </el-form-item>
      <el-form-item label="考试日期" required>
        <el-date-picker
          v-model="config.exam_begin"
          type="datetime"
          placeholder="选择考试日期"
          format="yyyy年MM月dd日 HH:mm"
          value-format="yyyy-MM-dd HH:mm"
          :picker-options="pickerOptions"
          @change="dateChange"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="考试时长(分钟)" required>
        <el-input-number
          v-model="config.exam_len"
          :min="15"
          controls-position="right"
        >
        </el-input-number>
      </el-form-item>
      <el-form-item label="考试备注">
        <el-input v-model="config.exam_note"></el-input>
      </el-form-item>
    </el-form>
    <el-button @click="done" >提交考试配置</el-button>
  </div>
</template>

<script>
const confirmFuc=function(data){}
export default {
    methods:{
        done()
        {
            this.action(this.config)
        },
        dateChange()
        {
          let d=new Date( this.config.exam_begin)
          if(d<=Date.now()+1000*60*20)
          {
            this.config.exam_begin=undefined
            this.$message({
              message:"请选择最少20分钟后的时间点",
              type:'error'
            })
          }
        }
    },
  data() {

    return {
        pickerOptions: {
          disabledDate(time) {
            const td=time.getTime();
            return  td< Date.now()- 3600 * 1000 * 24;
          },
          shortcuts: [{
            text: '明天',
            onClick(picker) {
              picker.$emit('pick', new Date().getTime()+ 3600 * 1000 * 24);
            }
          }, {
            text: '3天后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24*3);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },
      config: {
        exam_name: undefined,
        exam_begin: undefined,
        exam_len: 90,
        exam_note: undefined,
      },
    }
  },
  props:{
    action:confirmFuc
  }
};
</script>

<style>
</style>