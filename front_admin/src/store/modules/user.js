import { login, getInfo, api_log_out } from '@/api/user'
import { getToken, setToken, removeToken, removeAccess, setAccess, getAccess } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: ''
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
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        setAccess(response[0])
        commit('SET_TOKEN', response[1])
        setToken(response[1])

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    const user = JSON.parse(getToken())
    commit('SET_NAME', user.name)
    commit('SET_AVATAR', 'https://papers.co/wp-content/uploads/papers.co-bj56-art-wood-mountain-digital-day-1-wallpaper-300x300.jpg')
  },

  // user logout
  logout({ commit, state }) {
    let access=getAccess();
    removeAccess();
    removeToken() // must remove  token  first
    resetRouter()
    commit('RESET_STATE')
    api_log_out(access).then(res=>{

    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      let access=getAccess();
      removeAccess();
      removeToken() // must remove  token  first
      resetRouter()
      commit('RESET_STATE')
      api_log_out(access).then(res=>{
  
      })
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

