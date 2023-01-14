<template>
  <div>
    <el-transfer
      style="text-align: left; display: inline-block"
      v-model="selected"
      filterable
      :titles="['题库', '考试题目']"
      :button-texts="['加入试卷', '移出试卷']"
      :format="{
        noChecked: '${total}',
        hasChecked: '${checked}/${total}'
      }"
      @change="handleChange"
      :data="data">
      <span slot-scope="{ option }">{{ option.key }} - {{option.label}}</span>
      <!-- <el-button class="transfer-footer" slot="left-footer" size="small">操作</el-button>
      <el-button class="transfer-footer" slot="right-footer" size="small">操作</el-button> -->
    </el-transfer>
  </div>
</template>

<script>
import { CountQues, typeOf } from '@/api/question'
export default {
    data(){
        return {
            selected:[],
            data:[]
        }
    },
    methods:{
        
        format(d)
        {
            return '题目ID:'+d.que_id+' 题型:'+typeOf(d.que_type)+' 标题:'+d.title
        }
    },
    beforeUpdate(){
        CountQues().then(res=>{

        })
    }
}
</script>

<style>

</style>