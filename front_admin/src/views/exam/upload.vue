<template>
  <div>
    <el-steps :active="active" finish-status="success">
  <el-step title="配置考试信息"></el-step>
  <el-step title="上传考生信息"></el-step>
  <el-step title="选取考题"></el-step>
</el-steps>
<div v-if="active===0">
<config :action="configed"></config>
</div>
<div v-else-if="active===1">
    <student-input ref="st" :action="inputDone"></student-input>
</div>


  </div>
</template>

<script>
import Config from './config.vue'
import StudentInput from './StudentInput.vue'
export default {
  components: { StudentInput, Config },
    name:'ExamMain',
    data(){
        return {
            active:0,
            students:[],
            examconfig:undefined
        }
    },
    methods:{
        inputDone(data)
        {
            if(data&&data.length>0)
            {
                this.students=data
                this.active++
            }
            else
            {
                this.$message({
                    message:"数据非法",
                    type:'error'
                })
            }

        },
        configed(data)
        {
            if(data.exam_name&&data.exam_begin&&data.exam_len)
            {
                this.examconfig=data
                this.active++
            }
            else
            {
                this.$message({
                    message:"数据非法",
                    type:'error'
                })
            }

        }
    }

}
</script>

<style>

</style>