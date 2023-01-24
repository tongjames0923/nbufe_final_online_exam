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
<div v-else-if="active===2">
<select-question :cb="selectedQues"></select-question>
<el-button @click="uploadExam">上传试卷</el-button>
</div>

  </div>
</template>

<script>
import Config from './components/config.vue'
import SelectQuestion from './components/SelectQuestion.vue'
import StudentInput from './components/StudentInput.vue'
import {api_uploadExam} from '@/api/exam'
import { getToken } from '@/utils/auth'
export default {
  components: { StudentInput, Config, SelectQuestion },
    name:'ExamMain',
    data(){
        return {
            active:0,
            students:[],
            examconfig:undefined,
            questions:[]
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
        uploadExam()
        {
            let user= JSON.parse(getToken())
            api_uploadExam(user.id,this.examconfig,this.students,this.questions).then(res=>{
                this.$message({
                    message:"上传考试成功",
                    type:'success'
                })
                this.active=0;
            })
        },
        selectedQues(data)
        {
            this.questions=data
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