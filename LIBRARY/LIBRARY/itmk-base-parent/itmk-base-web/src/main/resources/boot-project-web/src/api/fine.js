import http from "@/utils/http";

//支付宝
export const aliPayApi = async(parm) =>{
    // http://localhost:9090/payment/goAlipay?merchantUserId=123456&merchantOrderId=654321
    return await http.get("http://localhost:8089/payment/goAlipay",parm);
}

// //支付宝
// export const getAliPayApi = async(parm) =>{
//     // http://localhost:9090/payment/goAlipay?merchantUserId=123456&merchantOrderId=654321
//     return await http.get("http://localhost:9090/payment/alipay",null);
// }

//获取罚款列表
export const getFineListApi = async(parm)=>{
    return await http.get("/api/fine/getFineList",parm);
}

//交罚款
export const payFineApi = async(parm)=>{
    return await http.get("/api/fine/payFine",parm);
}