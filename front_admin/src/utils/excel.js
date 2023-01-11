
export function readEXCEL(file)
{
        // //wb.SheetNames[0]是获取Sheets中第一个Sheet的名字
        // //wb.Sheets[Sheet名]获取第一个Sheet的数据
        // // document.getElementById("demo").innerHTML= JSON.stringify( XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]) );
        // let data1 =  JSON.stringify( XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]) );
        // console.log(data1)

        return new Promise(resolve => {
            let reader = new FileReader()
            reader.readAsBinaryString(file)
            reader.onload = ev => {
                resolve(ev.target.result)
            }
        })
}