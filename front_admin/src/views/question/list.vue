<template>
    <div>

        <el-input placeholder="请输入内容" v-model="text" class="input-with-select">
            <template slot="append">
                <el-button icon="el-icon-search" @click.prevent.native="search()" type="primary">搜索</el-button>

            </template>
            <el-select v-model="searchtype" slot="prepend" placeholder="请选择" style="width:200px">
                <el-option label="根据考题标题" value="1"></el-option>
                <el-option label="根据考题ID" value="2"></el-option>
                <el-option label="根据Tag名称" value="3"></el-option>
            </el-select>

        </el-input>

        <el-table max-height="750" :data="tableData" border style="width: 100%">
            <el-table-column fixed="left" prop="que_id" label="ID" width="120">
            </el-table-column>
            <el-table-column fixed="left" label="题型" prop="que_type" width="90" :filters="tableFilter"
                :filter-method="filterHandler">
                <template slot-scope="data">
                    <el-tag v-if="data.row.que_type == 0" type="warning">选择题</el-tag>
                    <el-tag v-else-if="data.row.que_type == 1" type="warning">填空题</el-tag>
                    <el-tag v-else-if="data.row.que_type == 2" type="warning">简答题</el-tag>
                    <el-tag v-else type="danger">错误</el-tag>
                </template>
            </el-table-column>
            <el-table-column  prop="title" label="标题" width="280">
                <template slot-scope="d">
                    <el-input v-model="d.row.title" @blur="changeTitle(d.row.que_id, d.row.title)"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="标签" fixed="left" width="150">
                <template slot-scope="data">
                    <el-select v-model="selected_tags[data.$index]" value-key="tag_name" multiple placeholder="请选择">
                        <el-option v-for="item in tags" :key="item.tag_name" :label="item.tag_name" :value="item">
                        </el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column label="创建者信息" width="180">
                <template slot-scope="scope">
                    <el-button type="info" @click="pul(scope.row.que_creator)">用户:{{
                        scope.row.que_creator
                    }}</el-button>
                </template>
            </el-table-column>
            <el-table-column label="正确率" :filters="acFilter" :filter-method="bigfilterHandler">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.answerd == 0" type="warning">信息不全</el-tag>
                    <el-progress v-else :percentage="Math.round(scope.row.answerd_right / scope.row.answerd * 100)"
                        :format="format"></el-progress>
                </template>
            </el-table-column>
            <el-table-column prop="use_time" label="使用次数" width="85" sortable>
            </el-table-column>
            <el-table-column label="公开" :filters='pbFilter' :filter-method="pbfilterHandler" fixed="right" width="50">
                <template slot-scope="scope">
                    <el-button type="text" size="small" v-if="scope.row.publicable == 1"
                        @click="changePublic(scope.$index, 0)">公开</el-button>
                    <el-button type="text" size="small" v-else @click="changePublic(scope.$index, 1)">私有</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="320" fixed="right">
                <template slot-scope="scope">
                    <el-button type="text" size="small" v-if="selectable" @click="select(scope.row)">选择本题</el-button>
                    <el-button type="text" size="small" @click="updateTag(scope.$index)">更新Tag</el-button>
                    <el-button type="text" size="small" @click="findAnswer(scope.row.que_id)">查看答案</el-button>
                    <el-button type="text" size="small" @click="showBody(scope.row.que_id)">查看题目</el-button>
                    <el-button type="text" size="small" @click="deleteQues(scope.row.que_id)">删除题目</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-drawer title="用户信息" :with-header="false" :visible.sync="drawer" :direction="direction" size="50%"
            style="padding:20px">
            <UserInfo ref="user" :editable="false"></UserInfo>
        </el-drawer>
        <el-dialog title="答案" :visible.sync="showAns">
            <answer ref="ans"></answer>
        </el-dialog>
        <el-dialog title="题目" :visible.sync="showques">
            <v-md-preview :text="ques_body"></v-md-preview>
        </el-dialog>
        <el-pagination @current-change="handleCurrentChange" :current-page.sync="cur" :page-size="per"
            layout="total, prev, pager, next" :total=total>
        </el-pagination>
    </div>
</template>
<script>
/* eslint-disable */
import UserInfo from '@/views/dashboard/components/userinfo.vue';
import TagList from '@/views/tag/list.vue';
import { api_getUnselect, getAllTags, getTagByQues } from '@/api/tag';
import { getUser } from '@/api/user'
import {api_getAnswer} from '@/api/answer'
import { list, CountQues, questionBody, api_changePublic, searchByTitle, searchByID, searchByTag, deleteQues, api_updateTags, api_changeTitle } from '@/api/question';
import Answer from './answer.vue';
import { getToken } from '@/utils/auth';
const SelectCallback = function (item) { }
export default
    {
        components: {
            UserInfo, TagList,
            Answer,
        },
        name: "QuestionList",
        props: {

            selectable: {
                type: Boolean,
                default: false
            },
            cb: {
                type: SelectCallback,
                default: (item) => {
                    console.log(item)
                }
            }
        },
        data() {
            return {
                text: "",
                searchtype: "1",
                tableData: [],
                total: -1,
                cur: 0,
                per: 30,
                drawer: false,
                direction: 'rtl',
                showAns: false,
                showques: false,
                ques_body: '',
                tableFilter: [{ text: '选择题', value: 0 }, { text: '填空题', value: 1 }, { text: '简答题', value: 2 }],
                acFilter: [{ text: '>20%', value: 20 }, { text: '>50%', value: 50 }, { text: '>80%', value: 80 }],
                pbFilter: [{ text: '公开', value: 1 }, { text: '私有', value: 0 }],
                tags: [],
                selected_tags: []
            };
        }
        ,
        mounted() {
            this.list();
            this.load();
        },
        methods:
        {

            changeTitle(que_id, title) {
                this.$confirm(
                    "确认将[" + que_id + "]题的标题修改为" + title + "吗？",
                    "确认修改标题",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    }
                ).then(() => {
                    api_changeTitle(que_id,title).then(res=>{
                        this.$message({ message: "更新成功", type: 'success' })
                    }).catch(err=>{
                        this.load();
                    })
                }).catch(()=>{
                    this.load();
                })
            },
            updateTag(index) {
                api_updateTags(this.tableData[index].que_id, this.selected_tags[index]).then(res => {
                    this.$message({ message: "更新成功", type: 'success' })
                }).catch(e => {
                    this.load();
                })
            },
            deleteQues(que_id) {
                const usr = JSON.parse(getToken());
                deleteQues(que_id, usr.id).then(res => {
                    this.$message({ message: "删除成功", type: 'success' })
                    this.list();
                    this.load();
                })
            },
            showBody(id) {
                this.showques = true;
                questionBody(id).then(res => {
                    this.ques_body = res;
                    this.$forceUpdate();

                }).catch(err => {
                    console.log(err);
                });
            },
            findAnswer(idx) {
                this.showAns = true
                api_getAnswer(idx).then(res=>{
                    this.$refs.ans.updateUI(res.type, res.answer_content);
                    this.$forceUpdate();
                })
            },
            pul(ix) {
                this.drawer = true
                getUser(ix).then(data => {

                    this.$refs.user.put(data);
                });
            },
            format(percentage) {
                return percentage === 100 ? '满' : `${percentage}%`;
            },
            changePublic(idx, pb) {
                let ids = this.tableData[idx].que_id;
                api_changePublic(ids, pb).then(res => {
                    this.tableData[idx].publicable = pb;
                });
            },
            list() {
                CountQues().then(res => {
                    this.total = res
                    this.cur = 1
                }).catch(error => {
                    console.log(error);
                });
            },
            handleCurrentChange(val) {
                list((val) * this.per, this.per).then(res => {
                    this.tableData = res
                })
            },
            load() {
                list((this.cur - 1) * this.per, this.per).then(res => {
                    this.tableData = res
                    this.settags();
                })
            },
            settags() {
                getAllTags().then(tgs => {
                    this.tags = tgs;
                })
                this.selected_tags = []
                for (let i = 0; i < this.tableData.length; i++) {
                    this.selected_tags.push(this.tableData[i].tags)
                }
            },
            select(val) {
                // if(val!==this.cur&&this.cb!=undefined)
                // {
                //     this.cb(val);
                // }
                // this.cur = val

                this.cb(val);
            },
            filterHandler(value, row, column) {
                const property = column['property'];
                return row[property] === value;
            },
            bigfilterHandler(value, row, column) {
                const v = Math.round(row.answerd_right / row.answerd * 100)
                return v > value;
            },
            pbfilterHandler(value, row, column) {
                return row.publicable === value;
            },
            search() {
                if (this.searchtype == 1) {
                    searchByTitle(this.text, 0, this.total).then(res => {
                        this.tableData = res;
                        this.settags();
                    })
                }
                else if (this.searchtype == 2) {
                    searchByID(this.text).then(res => {
                        this.tableData = res;
                        this.settags();
                    })
                }
                else if (this.searchtype == 3) {
                    searchByTag(this.text).then(res => {
                        this.tableData = res;
                        this.settags();
                    })
                }
            }
        }
    }


</script>