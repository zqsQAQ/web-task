import http from "@/utils/http"

//列表
export const getListApi = async(parm) => {
    return await http.get("/api/reader/list",parm);
}
//新增
export const addReaderApi = async(parm) =>{
    return await http.post("/api/reader", parm)
}
//编辑
export const editReaderApi = async(parm) =>{
    return await http.put("/api/reader", parm)
}
//删除
export const deleteReaderApi = async(parm) =>{
    return await http.delete("/api/reader",parm)
}
//根据学号查询读者信息
export const getByUserNameApi = async(parm) => {
    return await http.get("/api/reader/getByUserName",parm)
}
//注册读者
export const registerApi = async(parm) =>{
    return await http.post("/api/reader/register",parm)
}
//读者审核
export const applyReaderApi = async(parm) =>{
    return await http.put("/api/reader/applyReader",parm)
}
//删除某年份的毕业生（这里排除了存在未还书和未付款的学生）
export const deleteGraduatesApi = async(parm) =>{
    return await http.delete("/api/reader/deleteGraduates",parm)
}
//根据readerId查询读者信息
export const getByReaderIdApi = async(parm) =>{
    return await http.getRestApi("/api/reader/getByReaderId",parm)
}
