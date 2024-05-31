import http from "@/utils/http";

//列表
export const getListApi = async(parm) =>{
    return await http.get("/api/books/list",parm);
}
//查询图书类别的列表
export const getCateListApi = async(parm) =>{
    return await http.get("/api/category/cateList",null);
}
//新增
export const addBookApi = async(parm) =>{
    return await http.post("api/books",parm);
}
//编辑
export const editBookApi = async(parm) =>{
    return await http.put("/api/books",parm);
}
//删除
export const deleteBookApi = async(parm) =>{
    return await http.delete("/api/books",parm);
}
//ISBN
export const getByISBNApi = async(parm) =>{
    return await http.get("https://api.jike.xyz/situ/book/isbn/"+parm,
            {apikey:"12567.c3cfbb03e21403897d9bd7d58f75aa8f.1ec4a8d79892eeeda2c522f589b588f6"}); 
}
// export const getByISBNApi = async(parm) =>{
//     return await http.get("http://118.31.113.49/api/isbn/v1/index",parm); 
// }

//获得一个书的所有复印本的barcode
export const getBarcodeApi = async(parm) =>{
    return await http.get("/api/copies/list",parm);
}
//ISBN
// export const getByISBNApi = async(parm) =>{
//     return await http.get("http://118.31.113.49/api/isbn/v1/index",parm);
// }
