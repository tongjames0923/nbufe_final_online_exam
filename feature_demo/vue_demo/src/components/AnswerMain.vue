<template>
  <div>
    <div v-if="this.type == 0">
      <el-table :data="this.ques" style="width: 100%;margin: auto;">
        <el-table-column prop="text" label="选项内容">
        </el-table-column>
        <el-table-column prop="right" label="正确性">
          <template slot-scope="scope">
            <el-tag :type="scope.row.right === '1' ? 'success' : 'danger'">
              {{ scope.row.right === "1" ? '正确' : '错误' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div style="height:15px"></div> 
    </div>
    <div v-else-if="this.type == 1">
      <el-table :data="this.ques" style="width: 100% ;margin: auto;">
        <el-table-column prop="text" label="填空答案">
        </el-table-column>
        <el-table-column prop="equal" label="完全匹配">
          <template slot-scope="scope">
            <el-tag :type="scope.row.equal === '1' ? 'success' : 'danger'">
              {{ scope.row.equal === "1" ? '需要完全匹配' : '无需完全匹配' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div style="height:15px"></div>
    </div>
    <div v-else-if="this.type == 2">
      <el-input @input="oninput" type="textarea" placeholder="请输入简答题答案内容" v-model="ques[0]" maxlength="1024"
        show-word-limit disabled>
      </el-input>
    </div>
    <div v-else>
      <el-empty description="请选择题型"></el-empty>
    </div>
  </div>
</template>

<script>
/* eslint-disable */ 
export default {
  methods: {
    oninput() {
      this.ques[0] = this.temp;
    },
    updateUI(tp,ques)
    {
      this.type=tp;
      this.ques= JSON.parse(ques);
      this.$forceUpdate();
    }
  },
  data() {
    return {
      type: -1,
      ques: [],
      temp: {},
    };
  }


}
</script>

<style>

</style>