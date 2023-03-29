<!-- eslint-disable vue/multi-word-component-names -->
/* eslint-disable */
<template>
    <div>
        <el-radio-group v-model="type" size="small" @change="change">
            <el-radio-button label=0>{{ typename[0] }}</el-radio-button>
            <el-radio-button label=1>{{ typename[1] }}</el-radio-button>
            <el-radio-button label=2>{{ typename[2] }}</el-radio-button>
        </el-radio-group>
        <div style="height:15px"></div>
        <el-radio-group v-model="isopen" size="small" @change="change">
            <el-radio-button label=0>私有</el-radio-button>
            <el-radio-button label=1>公开</el-radio-button>
        </el-radio-group>
        <div style="height:15px"></div>
        <el-input v-model=title placeholder="题目标题"></el-input>
        <div style="height:15px"></div>
        <el-collapse>
            <el-collapse-item title="标签选择">
                <tag-list :selectable="true" ref="tags"></tag-list>
            </el-collapse-item>
            <el-collapse-item title="题面编辑器" name="1">
                <v-md-editor v-model="text" height="400px" @save=saveFile></v-md-editor>
            </el-collapse-item>
            <el-collapse-item title="答案编辑器" name="2">
                <div v-if="this.type == 0">
                    <el-table :data="ques" style="width: 100%">
                        <el-table-column label="选项内容" width="180">
                            <template slot-scope="data">
                                <el-input v-model="data.row.text"></el-input>
                            </template>
                        </el-table-column>
                        <el-table-column prop="right" label="正确性" width="100">
                            <template slot-scope="scope">
                                <el-tag :type="scope.row.right === '1' ? 'success' : 'danger'">
                                    {{ scope.row.right === "1" ? '正确' : '错误' }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="功能" width="180">
                            <template slot-scope="scope">
                                <el-button type="danger" round @click="deleteSelect(scope.$index)">删除</el-button>
                            </template>

                        </el-table-column>
                    </el-table>
                    <div style="height:15px"></div>
                    <el-button type="primary" icon="el-icon-edit" round @click="newSelect">新增</el-button>
                    <el-dialog title="选择单项" :visible.sync="select_item_visibility">
                        <el-input v-model="temp.text" placeholder="选项文本"></el-input>
                        <el-radio-group v-model="temp.right">
                            <el-radio label=1>正确</el-radio>
                            <el-radio label=0>错误</el-radio>
                        </el-radio-group>
                        <div style="height:10px"></div>
                        <el-button @click="inputSelect(temp.text, temp.right)">确认</el-button>
                    </el-dialog>
                </div>
                <div v-else-if="this.type == 1">
                    <el-table :data="ques" style="width: 100%">
                        <el-table-column  label="填空答案" width="180">
                            <template slot-scope="data">
                                <el-input v-model="data.row.text"></el-input>
                            </template>
                        </el-table-column>
                        <el-table-column prop="equal" label="完全匹配" width="100">
                            <template slot-scope="scope">
                                <el-tag :type="scope.row.equal === '1' ? 'success' : 'danger'">
                                    {{ scope.row.equal === "1" ? '需要完全匹配' : '无需完全匹配' }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="功能" width="180">
                            <template slot-scope="scope">
                                <el-button type="danger" round @click="deleteSelect(scope.$index)">删除</el-button>
                            </template>

                        </el-table-column>
                    </el-table>
                    <div style="height:15px"></div>
                    <el-button type="primary" icon="el-icon-edit" round @click="newFillBlank">新增</el-button>
                    <el-dialog title="填空答案" :visible.sync="fill_blank_visibility">
                        <el-input v-model="temp.text" placeholder="选项文本"></el-input>
                        <el-radio-group v-model="temp.equal">
                            <el-radio label=1>需要完全匹配</el-radio>
                            <el-radio label=0>无需完全匹配</el-radio>
                        </el-radio-group>
                        <div style="height:10px"></div>
                        <el-button @click="inputFillBlank(temp.text, temp.equal)">确认</el-button>
                    </el-dialog>
                </div>
                <div v-else-if="this.type == 2">
                    <el-input @input="oninput" type="textarea" placeholder="请输入简答题答案内容" v-model="temp" maxlength="1024"
                        show-word-limit>
                    </el-input>
                </div>
                <div v-else>
                    <el-empty description="请选择题型"></el-empty>
                </div>
            </el-collapse-item>
        </el-collapse>
        <div style="height:15px"></div>
        <el-button @click="upload()">提交</el-button>
    </div>
</template>

<script>
/* eslint-disable */
import TagAdd from "@/views/tag/add.vue"
import TagList from '@/views/tag/list.vue';
import {create_ques} from '@/api/question'
import { getToken } from '@/utils/auth';
export default {
    components: { TagAdd, TagList },
    data() {
        return {
            text: '',
            type: -1,
            typename: ["选择", "填空", "简答"],
            ques: [],
            temp: {},
            select_item_visibility: false,
            fill_blank_visibility: false,
            isopen: 1,
            title: "",
            addtag: false
        };
    },
    methods:
    {
        upload() {
            let arr = this.$refs.tags.getSelects();
            let files = new File([new Blob([this.text], { type: 'text/plain;chartset=UTF-8' })],
             "quesfile.md")
             var data=new FormData();
             const user=JSON.parse(getToken())
             data.append("type",this.type);
             data.append("creator",user.id);
             data.append("title",this.title)
             data.append("md",files)
             data.append("isopen",this.isopen)
             data.append("tags",arr),
             console.log(JSON.stringify(this.ques))
             data.append("answer",JSON.stringify(this.ques))
             create_ques(data).then(res=>{
                this.$message({
                        message: '新增题目成功',
                        type: 'success'
                    });
             });
        },
        oninput() {
            this.ques[0] = this.temp;
        },
        newFillBlank() {
            this.fill_blank_visibility = true;
        },
        inputFillBlank(text, equal) {
            this.ques.push({ "text": text, "equal": equal });
            this.fill_blank_visibility = false;
        },
        deleteSelect(index) {
            this.ques.splice(index, 1);
        },
        inputSelect(text, right) {
            this.ques.push({ "text": text, "right": right });
            this.select_item_visibility = false;
        },
        change() {
            ques = []
            if (type == 2)
                this.temp = ""
        },
        newSelect() {
            this.select_item_visibility = true;
        },

        saveFile(text, html) {
            let objectUrl = window.URL.createObjectURL(new Blob([text], { type: 'text/plain;chartset=UTF-8' }));
            let a = document.createElement('a');
            a.href = objectUrl;
            a.download = 'file.md';
            a.click()
            a.remove();
        }
    },
    mounted() {
        tagapi.getAllTags().then(res=>{
            const data=res.data;
            this.$refs.tags.updateData(data);
            this.$forceUpdate();
        })
    }
}
</script>

<style>

</style>