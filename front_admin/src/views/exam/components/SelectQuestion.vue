<template>
  <div>

    <el-collapse v-model="act">
      <el-collapse-item title="题库列表" name="1">
        <ques-list selectable :cb="selectcb"></ques-list>
      </el-collapse-item>
      <el-collapse-item title="试卷题目列表" name="2">\
        <el-table border max-height="350" :data="selected" style="width:100%">
          <el-table-column prop="que.que_id" width="75" label="题目ID"></el-table-column>

          <el-table-column label="题型" width="90" prop="que.que_type" :filters="tableFilter"
                :filter-method="filterHandler">
                <template slot-scope="data">
                    <el-tag v-if="data.row.que.que_type == 0" type="warning">选择题</el-tag>
                    <el-tag v-else-if="data.row.que.que_type == 1" type="warning">填空题</el-tag>
                    <el-tag v-else-if="data.row.que.que_type == 2" type="warning">简答题</el-tag>
                    <el-tag v-else type="danger">错误</el-tag>
                </template>
            </el-table-column>
          <el-table-column prop="que.title" label="题目标题">
          </el-table-column>
          <el-table-column  label="题目分值">
            <template slot-scope="dp">
              <el-input-number v-model="dp.row.score" controls-position="right"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="dp">
              <el-button type="text" size="small" @click="remove(dp.$index)">从试卷中移除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-collapse-item>
</el-collapse>

  </div>
</template>

<script>
import { CountQues, typeOf } from "@/api/question";
import quesList from "@/views/question/list.vue";
const action=(data)=>{}
export default {
  components: {
    quesList,
  },
  props:
  {
    cb:action
  },
  data() {
    return {
      selected: [],
      data: [],
      act:['1'],
      tableFilter: [{ text: '选择题', value: 0 }, { text: '填空题', value: 1 }, { text: '简答题', value: 2 }]
    };
  },
  methods: {
    selectcb(val)
    {
      for(let i=0;i<this.selected.length;i++)
      {
        if(this.selected[i].que===val)
        {
          this.$message({
            message:'该题已经存在',
            type:'warning'
          })
          return
        }
      }
      this.selected.push({score:5,que:val});
      if(this.cb)
      {
        let cbd=[]
        for(let i=0;i<this.selected.length;i++)
        {
          cbd.push({score:this.selected[i].score,ques_id:this.selected[i].que.que_id})
        }
        this.cb(cbd);
      }
    }
    ,
    remove(index)
    {

       this.selected.splice(index,1)
    },
    filterHandler(value, row, column) {
      const v=row.que.que_type
                return v === value;
            },
    format(d) {
      return (
        "题目ID:" +
        d.que_id +
        " 题型:" +
        typeOf(d.que_type) +
        " 标题:" +
        d.title
      );
    },
  },
  beforeUpdate() {
    CountQues().then((res) => {});
  },
};
</script>

<style>
</style>