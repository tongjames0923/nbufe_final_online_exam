<template>
    <div>
        <el-upload v-loading="!selected" ref="upload" :http-request="uploadfile" :auto-upload="true" :multiple="false"
            :limit="1" action="#" :file-list="files">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传excel文件</div>
        </el-upload>

        <el-select v-model="col[0]" placeholder="考生身份证列选择">
            <el-option v-for="item in options" :key="item.index" :label="item.item" :value="item.index">
            </el-option>
        </el-select>
        <el-select v-model="col[1]" placeholder="考生姓名列选择">
            <el-option v-for="item in options" :key="item.index" :label="item.item" :value="item.index">
            </el-option>
        </el-select>
        <el-select v-model="col[2]" placeholder="考生考号列选择">
            <el-option v-for="item in options" :key="item.index" :label="item.item" :value="item.index">
            </el-option>
        </el-select>
        <el-button @click="readall()">执行读取</el-button>
    </div>
</template>

<script>
import { readEXCEL } from "@/utils/excel"
import XLSX from "xlsx";
export default {
    data() {
        return {
            files: [],
            selected: true,
            col: [undefined, undefined, undefined],
            options: [],
            sheet: undefined,
            students:[]
        };
    },
    methods: {
        getHeaderRow(sheet) {
            let headers = []
            /* sheet['!ref']表示所有单元格的范围，例如从A1到F8则记录为 A1:F8*/
            const range = XLSX.utils.decode_range(sheet['!ref'])
            let C,
                R = range.s.r  /* 从第一行开始 */
            /* 按列进行数据遍历 */
            for (C = range.s.c; C <= range.e.c; ++C) {
                /* 查找第一行中的单元格 */
                const cell = sheet[XLSX.utils.encode_cell({ c: C, r: R })]
                let hdr = 'UNKNOWN ' + C // <-- 进行默认值设置
                if (cell && cell.t) hdr = XLSX.utils.format_cell(cell)
                headers.push({ index: C, item: hdr });
            }
            return headers
        },
        readstudents() {
            this.students.length=0
            const range = XLSX.utils.decode_range(this.sheet['!ref'])
            for(let i=range.s.r+1;i<=range.e.r;i++)
            {
                let vp=[]
                for(let j=0;j<this.col.length;j++)
                {
                    const cell = this.sheet[XLSX.utils.encode_cell({ c: this.col[j], r: i})]
                    if (cell && cell.t) 
                    {
                       let hdr = XLSX.utils.format_cell(cell)
                       vp.push(hdr)
                    }
                }
                this.students.push({id:vp[0], name:vp[1],number:vp[2]})
            }
            debugger
        },
        uploadfile(fileobject) {
            this.selected = false;
            let file = fileobject.file;
            readEXCEL(file).then(data => {

                let workBook = XLSX.read(data, { type: 'binary', cellDates: true });
                let workSheet = workBook.Sheets[workBook.SheetNames[0]]
                this.sheet = workSheet
                this.options = this.getHeaderRow(workSheet)
                // const da = XLSX.utils.sheet_to_json(workSheet)
                // console.log(da)
                // debugger
                this.selected = true;
            });
        }
    }
}
</script>

<style>

</style>