<template>
    <div>
        <el-input type="textarea" placeholder="请输入内容" v-model="textarea" maxlength="1000" @input="changeVal" show-word-limit>
        </el-input>
    </div>
</template>
<script>
const cb = (val, i) => { };
export default {
    name: 'ShortAnswer',
    props: {
        callback: cb,
        idx: Number
    },
    data() {
        return {
            textarea:''
        }
    },
    methods: {
        changeVal(val) {
            let arr = []
            arr.push(val);
            this.callback(arr, this.idx)
        }
    },
}
</script>