<template>
    <div>
        <el-table :data="exlist" style="width: 100%">
            <el-table-column label="考试ID" prop="exam_id">

            </el-table-column>
            <el-table-column label="考试名称">
                <template slot-scope="data">
                    <el-input v-model="data.row.exam_name"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="考试时间">
                <template slot-scope="data">
                    <el-date-picker v-model="data.row.exam_begin" type="datetime" placeholder="考试日期"
                        format="yyyy年MM月dd日 HH:mm" value-format="yyyy-MM-dd HH:mm">
                    </el-date-picker>
                </template>
            </el-table-column>
            <el-table-column label="考试时长">
                <template slot-scope="data">
                    <el-input-number v-model="data.row.exam_len" :min="15" controls-position="right">
                    </el-input-number>
                </template>
            </el-table-column>
            <el-table-column label="考试备注">
                <template slot-scope="data">
                    <el-input @blur="ue(4,data.row.exam_note,data.row.exam_id)" v-model="data.row.exam_note"></el-input>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination :current-page.sync="exlist" @current-change="handleCurrentChange" :page-size=page
            layout="total, prev, pager, next" :total=excount>
        </el-pagination>
    </div>
</template>

<script>

import { api_count, api_list, api_updateNote } from '@/api/exam'
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

        };
    },
    methods: {
        ue(what,value,id){
            let u=JSON.parse(getToken());
            if(what===4)
            {
                api_updateNote(value,u.id,id).then(res=>{
                    this.$message({
                        message:"修改成功",
                        type:'success'
                    })
                    // this.origin=this.exlist
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
            // this.origin=res
        })
    }
}
</script>

<style>

</style>