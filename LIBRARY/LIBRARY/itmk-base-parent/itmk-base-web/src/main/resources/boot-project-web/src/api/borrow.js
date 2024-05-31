import http from "@/utils/http";

//借书
export const borrowApi = async(parm) =>{
    return await http.post("/api/borrow",parm);
} 
//还书列表
export const returnBorrowApi = async(parm) =>{
    return await http.get("/api/borrow/getBorrowList",parm);
}
//还书
export const returnBooksApi = async(parm) =>{
    return await http.post("/api/borrow/returnBook",parm);
}
//异常还书
export const exceptionBooksApi = async(parm) =>{
    return await http.post("/api/borrow/exceptionReturnBook",parm);
}
//借阅列表
export const getLookBorrowListApi = async(parm) =>{
    return await http.get("/api/borrow/getLookBorrowList",parm)
}
//扫码借书
export const borrowByBarApi = async(parm) =>{
    return await http.post("/api/copies/borrowCopy",parm);
} 
//扫码还书
export const returnByBarApi = async(parm) =>{
    return await http.get("/api/copies/returnCopy",parm);
} 
//扫码异常还书
export const ExceptionReturnByBarApi = async(parm) =>{
    return await http.post("/api/copies/exceptionCopy",parm);
} 
//取预定的书
export const getReserveApi = async(parm) =>{
    return await http.getRestApi("/api/borrow//getReserve",parm);
} 
//取消预定
export const cancelReserveApi = async(parm) =>{
    return await http.getRestApi("/api/borrow//cancelReserve",parm);
} 
