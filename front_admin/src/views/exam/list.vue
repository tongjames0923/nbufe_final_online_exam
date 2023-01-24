<template>
    <div>
        <el-table :data="exlist" style="width: 100%">
            <el-table-column label="考试ID" prop="exam_id">
            </el-table-column>
            <el-table-column label="考试名称">
                <template slot-scope="data">
                    <el-input v-model="data.row.exam_name" @blur="ue(3,data.$index,data.row.exam_name,data.row.exam_id)" ></el-input>
                </template>
            </el-table-column>
            <el-table-column label="考试时间">
                <template slot-scope="data">
                    <el-date-picker v-model="data.row.exam_begin"
                    @blur="ue(1,data.$index,data.row.exam_begin,data.row.exam_id)" 
                    type="datetime" placeholder="考试日期"
                        format="yyyy年MM月dd日 HH:mm" value-format="yyyy-MM-dd HH:mm">
                    </el-date-picker>
                </template>
            </el-table-column>
            <el-table-column label="考试时长">
                <template slot-scope="data">
                    <el-input-number @blur="ue(2,data.$index,data.row.exam_len,data.row.exam_id)" v-model="data.row.exam_len" :min="15" controls-position="right">
                    </el-input-number>
                </template>
            </el-table-column>
            <el-table-column label="考试备注">
                <template slot-scope="data">
                    <el-input @blur="ue(4,data.$index,data.row.exam_note,data.row.exam_id)" v-model="data.row.exam_note"></el-input>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination :current-page.sync="exlist" @current-change="handleCurrentChange" :page-size=page
            layout="total, prev, pager, next" :total=excount>
        </el-pagination>
    </div>
</template>

<script>

import { api_count, api_list, api_updateBegin, api_updateLen, api_updateName, api_updateNote } from '@/api/exam'
import { getToken } from '@/utils/auth';

export default {
    name: 'examList',
    data() {
        return {
            exlist: [],
            // origin:[],
            excount: 0,
            page: 10,
            cur: 0,
            oldlist:[]

        };
    },
    methods: {
        makeConfirm(text,id,value)
        {
          return  this.$confirm('新值:'+value,'确认修改[考试ID'+id+']的'+text+'吗？',{
                confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        },
        rollback(index)
        {
            this.$message({
          showClose: true,
          message: '请刷新页面来恢复数据原样'
        });
        },
        ue(what,index,value,id){

            let u=JSON.parse(getToken());

            if(what===4)
            {
                this.makeConfirm('备注',id,value).then(()=>{
                    api_updateNote(value,u.id,id).then(res=>{
                    this.$message({
                        message:"修改成功",
                        type:'success'
                    })
                })
                }).catch(error=>{
                    this.rollback(index)
                })
            }
            else if(what===1)
            {
                this.makeConfirm('考试开始时间',id,value).then(()=>{
                    api_updateBegin(value,u.id,id).then(res=>{
                    this.$message({
                        message:"修改成功",
                        type:'success'
                    })
                })
                }).catch(error=>{
                    this.rollback(index)
                })
            }
            else if(what===3)
            {
                debugger
                this.makeConfirm('考试名称',id,value).then(()=>{
                    api_updateName(value,u.id,id).then(res=>{
                    this.$message({
                        message:"修改成功",
                        type:'success'
                    })
                })
                }).catch(error=>{
                    this.rollback(index)
                })
            }
            else if(what===2)
            {
                var that=this
                this.makeConfirm('考试时长',id,value).then(()=>{
                    api_updateLen(value,u.id,id).then(res=>{
                    this.$message({
                        message:"修改成功",
                        type:'success'
                    })
                })
                }).catch(error=>{
                    that.rollback(index)
                })
            }
        },
        updateCount() {
            let u = JSON.parse(getToken());
            api_count(u.id).then(res => {
                this.excount = res
            })
        },
        handleCurrentChange(val) {
            this.updateCount()
            api_list(val * this.page, this.page).then(res => {
                this.exlist = res
            // this.origin=res
            })
        }
    },
    mounted() {
        this.updateCount();
        api_list(0, this.page).then(res => {
            this.exlist = res
            this.oldlist=this.exlist
            // this.origin=res
        })
    }
}
</script>

<style>

</style>