<template>
    <div>
        <el-table :data="tags" style="width: 100%" highlight-current-row @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="tag_name" label="标签名称" width="180">
            </el-table-column>
            <el-table-column prop="tag_used" label="标签使用次数"></el-table-column>
            <el-table-column label="功能" width="180">
                <template slot-scope="scope">
                    <el-button type="danger" round @click="deleteThat(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <tag-add ref="addTag"></tag-add>
        <div style="height:15px"></div>
        <el-button @click="tagAdd()">新增标签</el-button>
    </div>

</template>
  
<script>
/* eslint-disable */
import axios from 'axios';
import TagAdd from './TagAdd.vue';
import tagapi from '@/api/tags'
export default {
    components: { TagAdd },
    name: "TagList",

    data() {
        return {
            tags: [],
            cur_tag: [],
        }
    },
    methods: {
        getSelects() {
            let arr = [];
            for (let i = 0; i < this.cur_tag.length; i++)
                arr.push(this.cur_tag[i].tag_name);
            return arr;
        },

        handleSelectionChange(val) {
            this.cur_tag = val
        },
        tagAdd() {
            this.$refs.addTag.show();
            this.getAlltag();
            this.$forceUpdate();
        },
        updateData(tags) {
            this.tags = tags;
            this.$forceUpdate();

        },
        getAlltag() {
            tagapi.getAllTags().then(res => {
                const data = res.data;
                this.tags=data;
                this.$forceUpdate();
            })
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
                    this.getAlltag();
                }
                else {
                    this.$message({
                        message: "删除失败",
                        type: "error"
                    });
                }
            });
        }
    }
};
</script>