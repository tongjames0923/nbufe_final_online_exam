<template>
    <div>
        <el-table :data="tags" style="width: 100%" highlight-current-row @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="tag_name" label="标签名称" width="180">
            </el-table-column>
            <el-table-column prop="tag_used" label="标签使用次数"></el-table-column>
            <el-table-column v-if="this.editable" label="功能" width="180">
                <template slot-scope="scope">
                    <el-button type="danger" round @click="deleteThat(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <TagAdd ref="addTag"></TagAdd>
        <div style="height:15px"></div>
        <el-button v-if="this.editable" @click="tagAdd()">新增标签</el-button>
    </div>

</template>
  
<script>
/* eslint-disable */
import {getAllTags,remove} from '@/api/tag'
import TagAdd from '@/views/tag/add'
export default {
    components: {TagAdd},
    name: "TagList",

    data() {
        return {
            tags: [],
            cur_tag: [],
        }
    },
    mounted(){
        this.getAlltag();
    },
    props:
    {
        editable:{
            type:Boolean,
            default:true
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
            this.$refs.addTag.show(()=>{
                this.getAlltag();
            this.$forceUpdate();
            });
        },
        updateData(tags) {
            this.tags = tags;
            this.$forceUpdate();

        },
        getAlltag() {
            getAllTags().then(res => {
                this.tags=res;
                this.$forceUpdate();
            })
        },
        deleteThat(index) {
            remove(this.tags[index].tag_name).then(res=>{
                this.$message({
                        message: "删除成功",
                        type: "success"
                    });
                    this.getAlltag();
            });
        }
    }
};
</script>