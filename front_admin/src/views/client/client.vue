<template>
    <div id="app">
        <div v-if="!useruid">
            <el-input v-model="user_id" placeholder="学号"></el-input>
            <el-input v-model="user_name" placeholder="姓名"></el-input>
            <el-input v-model="user_num" placeholder="考号"></el-input>
            <el-select v-model="exam_selected" placeholder="请选择">
                <el-option v-for="(item, index) in exam_list" :key="item.exam_name" :label="item.exam_name" :value="index">
                </el-option>
            </el-select>
            <el-button size="medium" round @click="pullExams">{{ login_btn_text }}</el-button>
        </div>
        <div v-else>
            <el-collapse v-for="(item, index) in examData.questions" :key="index">
                <el-collapse-item :title="item.detail.title">
                    <v-md-preview :text="item.detail.que_file"></v-md-preview>
                    <div v-if="item.detail.que_type === 0">
                        <el-select v-model="checkData[index].text" multiple placeholder="请选择">
                            <el-option v-for="(val,index) in JSON.parse(item.detail.answer_data)" :key="index" :label="val.text" :value="val.text">
                            </el-option>
                        </el-select>
                    </div>
                    <div v-else-if="item.detail.que_type === 1">
                        <FillBlank :callback="fb_cb" :idx="index" :cnt="JSON.parse(item.detail.answer_data).length"></FillBlank>
                    </div>
                    <div v-else-if="item.detail.que_type === 2">
                        <ShortAnswer :callback="fb_cb" :idx="index"></ShortAnswer>
                    </div>
                </el-collapse-item>
            </el-collapse>
            <el-button size="medium" round @click="submit">交卷</el-button>

        </div>
    </div>
</template>
  
<script>
import { api_getMyExams, api_getExam,api_submitExam } from '@/api/client'
import FillBlank from './components/FillBlank.vue';
import ShortAnswer from './components/shortAnswer.vue';
export default {
    name: "ExamClient",
    data() {
        return {
            useruid: undefined,
            user_name: undefined,
            user_num: undefined,
            user_id: undefined,
            login_btn_text: "拉取考试",
            exam_list: [],
            exam_selected: null,
            exam_id: undefined,
            examData: undefined,
            checkData: []
        };
    },
    methods: {
        submit() {
            console.log(this.checkData);
            api_submitExam(this.exam_id,this.useruid,this.checkData).then(res=>{
                alert("交卷成功");
            })
        },
        fb_cb(val,i)
        {
            this.checkData[i].text=val
        },
        pullExams() {
            {
                if (this.exam_selected == null) {
                    api_getMyExams(this.user_name, this.user_num, this.user_id).then(res => {
                        this.exam_list = res;
                        this.login_btn_text = "登陆";
                    });
                }
                else {
                    this.useruid = this.exam_list[this.exam_selected].uid;
                    this.exam_id = this.exam_list[this.exam_selected].exam_id;
                    api_getExam(this.useruid, this.exam_id).then(res => {
                        this.examData = res;
                        this.checkData = [];
                        for (let i = 0; i < this.examData.questions.length; i++) {
                            let q = this.examData.questions[i];
                            this.checkData.push({ queid: q.ques_id, text: [] });
                        }
                        debugger;
                    });
                }
            }
        }
    },
    components: { FillBlank, ShortAnswer }
}
</script>
  