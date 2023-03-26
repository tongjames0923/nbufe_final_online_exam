import XLSX from "xlsx";
export async function readEXCEL(file,sheetIndex)
{
        // //wb.SheetNames[0]是获取Sheets中第一个Sheet的名字
        // //wb.Sheets[Sheet名]获取第一个Sheet的数据
        // // document.getElementById("demo").innerHTML= JSON.stringify( XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]) );
        // let data1 =  JSON.stringify( XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]) );
        // console.log(data1)
        let doc = await readFile(file)
        let workBook = XLSX.read(doc, { type: "binary", cellDates: true });
        let workSheet = workBook.Sheets[workBook.SheetNames[sheetIndex]];
        return workSheet;
}
function readFile(file)
{
    return new Promise(resolve => {
        let reader = new FileReader()
        reader.readAsBinaryString(file)
        reader.onload = ev => {
            resolve(ev.target.result)
        }
    })
}
export function getRowHead(sheet)
{
    let headers = [];
      /* sheet['!ref']表示所有单元格的范围，例如从A1到F8则记录为 A1:F8*/
      const range = XLSX.utils.decode_range(sheet["!ref"]);
      let C,
        R = range.s.r; /* 从第一行开始 */
      /* 按列进行数据遍历 */
      for (C = range.s.c; C <= range.e.c; ++C) {
        /* 查找第一行中的单元格 */
        const cell = sheet[XLSX.utils.encode_cell({ c: C, r: R })];
        let hdr = "UNKNOWN " + C; // <-- 进行默认值设置
        if (cell && cell.t) hdr = XLSX.utils.format_cell(cell);
        headers.push({ index: C, item: hdr });
      }
      return headers;
}
export function excel_each(sheet,func)
{
     const range = XLSX.utils.decode_range(sheet["!ref"]);
      for (let i = range.s.r + 1; i <= range.e.r; i++) {
        let vp = [];
        for (let j = range.s.c; j <= range.e.c; j++) {
          const cell =
            sheet[XLSX.utils.encode_cell({ c:j, r: i })];
          if (cell && cell.t) {
            let hdr = XLSX.utils.format_cell(cell);
            vp.push(hdr);
          }
        }
        func(vp)
    }
}