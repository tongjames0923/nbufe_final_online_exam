<template>
<div>
    <el-input v-for="item in datas" v-model="item.text"  placeholder="请填答案" @input="changeVal"></el-input>
</div>
</template>
<script>
const cb=(val,i)=>{};
export default {
  name: 'FillBlank',
  props:{
    cnt:Number,
    callback:cb,
    idx:Number
  },
  data() {
    return {
        datas:[]
    }
  },
  mounted() {
    for(let i=0;i<this.cnt;i++)
    this.datas.push({text:''});
    debugger
  },
  methods: {
    changeVal(val)
    {
        let arr=[]
        for(let i=0;i<this.datas.length;i++)
        arr.push(this.datas[i].text);
        this.callback(arr,this.idx)
    }
  },
}
</script>