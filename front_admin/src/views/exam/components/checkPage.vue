<template>
  <el-row v-loading='loading'>
    <el-collapse v-for="(val, index) in this.datas" :key="index">
      <el-collapse-item :title="val.examUser.name + '的试卷'">
        <el-descriptions title="用户信息">
          <el-descriptions-item label="唯一考试编号">{{
            val.examUser.uid
          }}</el-descriptions-item>
          <el-descriptions-item label="考号">{{
            val.examUser.number
          }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{
            val.examUser.id
          }}</el-descriptions-item>
        </el-descriptions>
        <div style="height: 15px;"></div>
        <el-table :data="val.replyList"        
        >
          <el-table-column prop="question" label="题号" fixed="left" width="120"></el-table-column>
          <el-table-column  label="分值" fixed="left" width="220">
            <template slot-scope="d">
                <el-input-number v-model="d.row.score" @blur="updateSocre(d.row.score,d.row.replyId)" :min="0"></el-input-number>            </template>

          </el-table-column>
          <el-table-column label="回答详情">
            <template slot-scope="d">
                <el-table :data="d.row.replyList"         :row-class-name="tableRowClassName">
                <el-table-column label="标准答案" prop="answerWord"></el-table-column>
                <el-table-column label="学生回答" prop="replyText"></el-table-column>
                </el-table>
            </template>
          </el-table-column>
        </el-table>
      </el-collapse-item>
    </el-collapse>
  </el-row>
</template>

<script>
import { api_listCheck, api_updateCheckScore } from "@/api/exam";
export default {
  data() {
    return {
        loading:false,
      datas: [],
      copys:[],
    };
  },
  methods: {
    tableRowClassName({row, rowIndex}) {
        if(row.sign==="1")
        return 'success-row'
      },
      updateSocre(s,er)
      {
        api_updateCheckScore(s,er).then((res)=>{
            this.copys=this.datas;
        }).catch(err=>{
        }).finally(()=>{
            this.$forceUpdate()
        })
      },
    change(examid) {
        this.loading=true
        this.exam_id=examid
      api_listCheck(examid).then((res) => {
        this.datas = res;
        this.copys=res;
        this.$forceUpdate();
      }).finally(()=>{
        this.loading=false;
      });
    },
  },
};
</script>

<style>
  .el-table .success-row {
    background: #f0f9eb;
  }
  .el-table .warning-row {
    background: rgb(207, 78, 31);
  }
</style>