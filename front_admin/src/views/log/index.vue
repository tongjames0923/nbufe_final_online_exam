<template>
    <div style="margin-top: 15px; margin-left: 15px;">
        <div v-if="hasRights">
            <el-select v-model="type" placeholder="请选择搜索类型" style="width: 320px; margin-right: 20px;">
            <el-option v-for="item in selectables" :key="item.index" :label="item.label" :value="item.index">
            </el-option>
        </el-select>
        <el-input v-model="input" placeholder="请输入搜索内容" style="margin-right: 15px; width: 350px;"></el-input>
        <el-button icon="el-icon-search" @click.prevent.native="search()" type="primary">搜索日志</el-button>
            <el-table :data="logData" style="width: 100%">
                <el-table-column prop="log_begin" label="调用日期" width="180">
                </el-table-column>
                <el-table-column prop="log_invoker" label="调用者信息" width="180">
                </el-table-column>
                <el-table-column prop="log_type" label="日志类型">
                </el-table-column>
                <el-table-column prop="log_function" label="调用方法">
                </el-table-column>
                <el-table-column prop="log_params" label="调用参数">
                </el-table-column>
                <el-table-column prop="log_error" label="错误">
                </el-table-column>
            </el-table>
        </div>
        <div v-else>
            您没有权限查看系统日志
        </div>
    </div>
</template>
<script>
import { getToken } from '@/utils/auth';
import {api_log} from '@/api/log'
/* eslint-disable */
export default
    {
        name: "LogList",
        data() {
            return {
                type: 0,
                input: "",
                logData:[],
                hasRights: false,
                selectables: [
                    {
                        index: 1,
                        label: "根据调用者用户名或者ID",
                    },
                    {
                        index: 0,
                        label: "根据日志类型"
                    }
                ]
            };
        }
        ,
        beforeMount() {
            this.hasRights = JSON.parse(getToken()).level >= 2
        },
        methods: {
            search()
            {
                api_log(this.type,this.input).then(res=>{
                    this.logData=res;
                })
            }
        },
    }


</script>