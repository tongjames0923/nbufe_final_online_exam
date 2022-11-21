<template>
    <el-table :data="tags" style="width: 100%">
        <el-table-column prop="tag_name" label="标签名称" width="180">
        </el-table-column>
        <el-table-column prop="tag_used" label="标签使用次数"></el-table-column>
        <el-table-column label="功能" width="180">
            <template slot-scope="scope">
                <el-button type="danger" round @click="deleteThat(scope.$index)">删除</el-button>
            </template>

        </el-table-column>
    </el-table>
</template>
  
<script>
/* eslint-disable */
import axios from 'axios';
export default {
    name: "TagList",

    data() {
        return {
            tags: []
        }
    },
    methods: {

        updateData() {
            axios({
                url: this.$baseUrl + "tag/list",
                method: "get"
            }).then(res => {
                if (res.data.code == this.$code_success) {
                    this.tags = res.data.data
                }
            });
        },
        deleteThat(index) {
            axios({
                url: this.$baseUrl + "tag/remove",
                method: "get",
                params: { tag: this.tags[index].tag_name }
            }).then(res => {
                if (res.data.code == this.$code_success) {
                    this.$message({
                        message: "删除成功",
                        type: "success"
                    });
                    this.updateData();
                }
                else {
                    this.$message({
                        message: "删除失败",
                        type: "error"
                    });
                }
            });
        }
    },
    mounted() {
        this.updateData();
    }
};
</script>