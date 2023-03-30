<template>
    <div style="margin-top: 15px; margin-left: 15px;">
        <div v-if="hasRights">
            <el-select v-model="type" placeholder="请选择搜索类型" style="width: 320px; margin-right: 20px;">
                <el-option v-for="item in selectables" :key="item.index" :label="item.label" :value="item.index">
                </el-option>
            </el-select>
            <el-input v-model="input" placeholder="请输入搜索内容" style="margin-right: 15px; width: 350px;"></el-input>
            <el-button icon="el-icon-search" @click.prevent.native="search()" type="primary"
                style="margin-right: 10px;">搜索日志</el-button>
            条数：
            <el-input-number v-model="MaxPage" :step="2"></el-input-number>
            <el-table :data="logData" style="width: 100%" v-if="type != 3">

                <el-table-column prop="log_begin" label="调用日期" width="220">
                </el-table-column>
                <el-table-column prop="log_invoker" label="调用者信息" width="180">
                </el-table-column>
                <el-table-column label="耗时" width="80">
                    <template slot-scope="data">
                        <el-tag v-if="data.row.cost < 100" type="success">{{ data.row.cost }}ms</el-tag>
                        <el-tag v-else-if="data.row.cost < 500">{{ data.row.cost }}ms</el-tag>
                        <el-tag v-else type="danger">{{ data.row.cost }}ms</el-tag>
                    </template>
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
            <el-table :data="log_cost_data" style="width: 100%" v-else-if="type == 3">
                <el-table-column label="方法名" prop="function"></el-table-column>
                <el-table-column label="平均耗时" prop="avg_cost"></el-table-column>
            </el-table>
        </div>
        <div v-else>
            您没有权限查看系统日志
        </div>
    </div>
</template>
<script>
import { getToken } from '@/utils/auth';
import { api_log, api_log_top } from '@/api/log'
/* eslint-disable */
export default
    {
        name: "LogList",
        data() {
            return {
                type: 0,
                input: "",
                logData: [],
                log_cost_data:[],
                hasRights: false,
                MaxPage: 100,
                selectables: [
                    {
                        index: 0,
                        label: "根据日志类型"
                    },
                    {
                        index: 1,
                        label: "根据调用者用户名或者ID",
                    },
                    {
                        index: 2,
                        label: "根据调用方法名"
                    },
                    {
                        index: 3,
                        label: '耗时Top'
                    }
                ]
            };
        }
        ,
        beforeMount() {
            this.hasRights = JSON.parse(getToken()).level >= 2
        },
        methods: {
            search() {
                if (this.type == 3) {
                    api_log_top(this.MaxPage).then(res => {
                        this.log_cost_data = res;
                    })
                    return;
                }
                api_log(this.type, this.input, this.MaxPage).then(res => {
                    this.logData = res;
                })
            }
        },
    }


</script>