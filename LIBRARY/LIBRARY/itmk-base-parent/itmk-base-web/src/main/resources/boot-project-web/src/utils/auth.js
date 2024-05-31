import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  // return Cookies.get(TokenKey)    //token太长Cookies里面存不下
  return sessionStorage.getItem(TokenKey);
}

export function setToken(token) {
  // return Cookies.set(TokenKey, token)  //token太长Cookies里面存不下
  return sessionStorage.setItem(TokenKey,token);
}

export function removeToken() {
  // return Cookies.remove(TokenKey)   //token太长Cookies里面存不下
  return sessionStorage.clear();
}

//存储用户id
export function setUserId(userId){
  return Cookies.set("userId",userId);
}

export function getUserId(){
  return Cookies.get("userId");
}

export function removeUserId(){
  return Cookies.remove("userId");
}


