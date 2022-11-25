<template>
    <div>
        <el-input placeholder="请输入内容" v-model="text" class="input-with-select">
            <el-select v-model="searchtype" slot="prepend" placeholder="请选择">
                <el-option label="根据考题标题" value="1"></el-option>
                <el-option label="根据考试ID" value="2"></el-option>
                <el-option label="全部" value="3"></el-option>
                <el-option label="根据Tags" value="3"></el-option>

            </el-select>
            <el-button icon="el-icon-search"></el-button>
        </el-input>

        <el-table :data="tableData" border style="width: 100%">
            <el-table-column fixed prop="que_id" label="ID" width="50" >
            </el-table-column>
            <el-table-column label="题型" prop="que_type" width="120" fixed>
                <template slot-scope="data">
                    <el-tag v-if="data.row.que_type==0"  type="warning">选择题</el-tag>
                    <el-tag v-else-if="data.row.que_type==1" type="warning">填空题</el-tag>
                    <el-tag v-else-if="data.row.que_type==2" type="warning">简答题</el-tag>
                    <el-tag v-else type="danger">错误</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" width="120" fixed>
            </el-table-column>
            <el-table-column label="创建者信息" width="120">
                <template slot-scope="scope">
                    <el-button type="info" @click="pul(scope.row.que_creator)">用户:{{scope.row.que_creator}}</el-button>
                </template>
            </el-table-column>
            <el-table-column label="正确率" width="300">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.answerd == 0" type="warning">信息不全</el-tag>
                    <el-progress v-else :percentage="Math.round(scope.row.answerd_right / scope.row.answerd * 100)"
                        :format="format"></el-progress>
                </template>
            </el-table-column>
            <el-table-column prop="use_time" label="使用次数" width="120">
            </el-table-column>
            <el-table-column label="公开性" width="120" fixed>
                <template slot-scope="scope">
                    <el-button icon="el-icon-unlock"  v-if="scope.row.publicable == 1" @click="changePublic(scope.$index, 0)">公开</el-button>
                    <el-button icon="el-icon-lock"  v-else @click="changePublic(scope.$index, 1)">私有</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="findTag(scope.$index)">查看Tag</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-drawer title="我是标题"
        :with-header="false" :visible.sync="drawer" :direction="direction" size="50%" style="padding:20px">
            <user-info ref="user"></user-info>
        </el-drawer>

        <el-pagination @current-change="handleCurrentChange" :current-page.sync="cur" :page-size="per"
            layout="total, prev, pager, next" :total=total>
        </el-pagination>
    </div>
</template>
<script>
/* eslint-disable */
import req from '@/util/request'
import userapi from "@/api/user"
import UserInfo from './UserInfo.vue';
export default
    {
        components: { UserInfo },
        name: "QuestionList",
        data() {
            return {
                text: "",
                searchtype: 3,
                tableData: [],
                total: -1,
                cur: 0,
                per: 2,
                drawer: false,
                direction: 'rtl',
            };
        }
        ,
        mounted() {
            this.list();
            this.load();
            console.log();
        },
        methods:
        {
            pul(ix) {
                this.drawer=true
                userapi.getUser(ix).then(data=>{
                    this.$refs.user.put(data.data);
                    this.$forceUpdate();
                });
            },
            findTag(idx) {

            },
            format(percentage) {
                return percentage === 100 ? '满' : `${percentage}%`;
            },
            changePublic(idx, pb) {
                let ids=this.tableData[idx].que_id;
                req.get({
                    url: "question/updatePublic",
                    params: {
                        ques: ids,
                        publicable: pb
                    },
                }).then(res => {
                    this.tableData[idx].publicable = pb;
                });
            },
            list() {
                req.get({  url: "question/questionCount" }).then(res => {
                    this.total = res.data
                    this.cur = 1
                }).catch(error => {
                    console.log(error);
                });
            },
            load() {
                req.get({
                    url: "question/list",
                    params: {
                        from: (this.cur - 1) * this.per,
                        num: this.per
                    }
                }).then(res => {
                    this.tableData = res.data
                })

            },
            handleCurrentChange(val) {
                this.cur = val;
                this.load();
            },
            search() {
                // if (this.searchtype == 1) {

                // }
                // else if (this.searchtype == 2) {

                // }
                // else {

                // }
            }
        }
    }


</script>