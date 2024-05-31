import { loginApi, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken, setUserId, getUserId,setReaderId,getReaderId } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    roles: []
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, userType } = userInfo
    return new Promise((resolve, reject) => {
      //api里面的user.js 里面的 loginApi方法
      loginApi({ username: username.trim(), password: password , userType:userType}).then(response => {
        const { data } = response
        console.log("login successfully")
        console.log(data)
        //登录返回之后，把token存到vuex里面
        commit('SET_TOKEN', data.token)
        //把token存到cookies里面
        setToken(data.token)
        setUserId(data.userId)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  //获取用户的权限信息和自己的信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo({userId:getUserId()}).then(response => {
        console.log("获取用户权限字段")
        console.log(response)
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { roles, name, avatar } = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }

        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

