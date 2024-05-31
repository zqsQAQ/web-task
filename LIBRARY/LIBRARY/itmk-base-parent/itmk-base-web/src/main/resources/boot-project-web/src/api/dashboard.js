import http from "@/utils/http";

//管理员面板
//查询所有读者的数量
export const getAllMembersNumApi = async() =>{
    return await http.get("/api/reader/getMemberNum",null);
}
//查询所有书的数量(不是复印本)
export const getAllBooksNumApi = async() =>{
    return await http.get("/api/books/getBookNum",null);
}
//查询所有书的数量(复印本)
export const getAllCopyNumApi = async() =>{
    return await http.get("/api/copies/getCopyNum",null);
}
//查询所有未还书的数量(借书总数)
export const getAllBooksNotReturnNumApi = async() =>{
    return await http.get("/api/borrow/getAllBorrowNum",null);
}
//查询历史所有被借书的数量
export const getAllHistoryBorrowNumApi = async() =>{
    return await http.get("/api/borrow/getAllHistoryBorrowNum",null);
}
//查询所有丢失的书的数量
export const getAllLostNumApi = async() =>{
    return await http.get("/api/borrow/getLost",null);
}
//查询所有损坏的书的数量
export const getDamageNumApi = async() =>{
    return await http.get("/api/borrow/getDamage",null);
}
//查询未支付罚款总数(不是金额)
export const getUnpaidNumApi = async() =>{
    return await http.get("/api/fine/getUnpaidNum",null);
}
//查询已经支付的罚款总数(不是金额)
export const getPaidNumApi = async() =>{
    return await http.get("/api/fine/getPaidNum",null);
}
//查询未支付罚款金额
export const getUnpaidFeeApi = async() =>{
    return await http.get("/api/fine/getUnpaidFee",null);
}
//查询已经支付的罚款金额
export const getPaidFeeApi = async() =>{
    return await http.get("/api/fine/getPaidFee",null);
}



//读者面板
//查询某用户未还书的数量
export const getReaderBorrowNumApi = async(parm) =>{
    return await http.getRestApi("/api/borrow/getReaderBorrowNum",parm);
}
//查询某用户未支付的罚款数量
export const getReaderUnpaidApi = async(parm) =>{
    return await http.getRestApi("/api/reader/checkUnpaid",parm);
}
//查询某用户历史已借书的数量
export const getReaderHistoryBorrowNumApi = async(parm) =>{
    return await http.getRestApi("/api/borrow/getReaderHistoryBorrowNum",parm);
}
//查询某用户总的的罚款数量
export const getReaderAllFineApi = async(parm) =>{
    return await http.getRestApi("/api/reader/checkAllFines",parm);
}
//查询某用户上一次还的书
export const getReaderLastReturnApi = async(parm) =>{
    return await http.getRestApi("/api/reader/checkLastReturn",parm);
}
//查询某用户上一次借的书
export const getReaderLastBorrowApi = async(parm) =>{
    return await http.getRestApi("/api/reader/checkLastBorrow",parm);
}
//查询某用户总罚款金额
export const getReaderTotalFeeApi = async(parm) =>{
    return await http.getRestApi("/api/fine/getReaderTotalFee",parm);
}
//查询某用户未支付的罚款金额
export const getReaderUnpaidFeeApi = async(parm) =>{
    return await http.getRestApi("/api/fine/getReaderUnpaidFee",parm);
}



//读者还书通知
export const unReturnNoticeApi = async(parm) =>{
    return await http.getRestApi("/api/reader/UnreturnNotice",parm);
}
//读者交罚款通知
export const unpaidNoticeApi = async(parm) =>{
    return await http.getRestApi("/api/reader/UnpaidNotice",parm);
}
//读者待取书通知
export const reserveNoticeApi = async(parm) =>{
    return await http.getRestApi("/api/borrow/reserveNotice",parm);
}

