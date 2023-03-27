<template>
    <div style="margin-top: 15px;">
        <div v-if="authable">
            <el-row >
                <el-col span="24">
                    <el-autocomplete class="inline-input" style="width: 320px;" v-model="selected_user"
                        :fetch-suggestions="querySearch" placeholder="请输入您想管理的用户名" :trigger-on-focus="false"
                        @select="handleSelect"></el-autocomplete>
                </el-col>
            </el-row>
            <el-row >
                <el-col span="24" type="flex" class="row-bg" justify="space-between">
                    <el-radio-group style="margin-top: 15px;" v-if="info" v-model="info.level"
                    
                        size="small" @input="changeLevel(info)">
                        <el-radio-button :disabled="user.id === info.id || info.level == 2"
                            label=-1>未激活</el-radio-button>
                        <el-radio-button :disabled="user.id === info.id || info.level == 2"
                            label=0>标准激活用户</el-radio-button>
                        <el-radio-button :disabled="user.id === info.id || info.level == 2"
                            label=1>资源写入权限用户</el-radio-button>
                        <el-radio-button :disabled="user.id === info.id || info.level == 2"
                            label=2>高级管理员</el-radio-button>
                    </el-radio-group>
                </el-col>
                <el-col span="24">
                    <el-descriptions title="用户信息" v-if="info" style="margin-top: 15px;">
                        <el-descriptions-item label="用户名">{{ info == undefined ? '空' : info.name
                        }}</el-descriptions-item>
                        <el-descriptions-item label="手机号">{{ info == undefined ? '空' : info.phone
                        }}</el-descriptions-item>
                        <el-descriptions-item label="电子邮箱">{{ info == undefined ? '空' : info.email
                        }}</el-descriptions-item>
                        <el-descriptions-item label="联系地址">{{ info == undefined ? '空' : info.address
                        }}</el-descriptions-item>
                        <el-descriptions-item label="备注">{{ info == undefined ? '空' :info.note
                        }}</el-descriptions-item>
                    </el-descriptions>
                </el-col>
            </el-row>

        </div>
        <div v-else>您没有权限操作其他用户</div>
    </div>
</template>
    
<script>
import { getToken } from '@/utils/auth';
import { allUserCount, api_findUserByName, getUser, pullUserList, updateLevel } from '@/api/user';
export default {
    name: "UserConfigPage",
    data() {
        return {
            selected_user: '',
            info: undefined,
            tableData: [],
            authable: false,
            total: -1,
            cur: 0,
            per: 30,
            user: undefined
        };
    },
    methods: {
        querySearch(queryString, cb) {
            api_findUserByName(queryString).then(res => {
                let result = []
                for (let i = 0; i < res.length; i++) {
                    result.push({ value: "姓名:" + res[i].name + ",备注:" + res[i].note, item: res[i] })
                }
                cb(result)
            })
        },
        handleSelect(item) {
            let appUser = item.item;
            this.info = appUser
            this.selected_user=''
        },
        changeLevel(data) {
            console.log(data)
            updateLevel(this.user.id, data.id, data.level).then(res => {

            }).catch(e=>{
                getUser(data.id).then(res=>{
                    this.info=res;
                })
            })
        }
    },
    mounted() {
        this.user = JSON.parse(getToken());
        this.authable = this.user.level === 2
    },
}
</script>
    
<style scoped></style>