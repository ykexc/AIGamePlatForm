import {defineStore} from 'pinia'
import {get} from "@/util/request";


interface UserAttribute {
    id: string
    username: string
    photo: string
    token: string
    is_login: boolean
    pulling_info: boolean
}


export default defineStore('user', {
    state: (): UserAttribute => {
        return {
            id: "",
            username: "",
            photo: "",
            token: "",
            is_login: false,
            pulling_info: true,
        }
    },
    actions: {

        UpdateState(isLogin: boolean) {
            this.is_login = isLogin
        },

        UpdateUser(user: User) {
            this.id = user.id
            this.username = user.username
            this.photo = user.photo
            this.is_login = user.is_login
        },
        UpdateToken(token: string) {
            this.token = token
        },
        Logout() {
            localStorage.removeItem("jwt_token")
            this.$reset()
        },
        UpdatePullingInfo(pulling_info: boolean) {
            this.pulling_info = pulling_info
        },
        getInfo(data: Resp) {
            get({
                url: '/api/user/account/info/', isAuth: true, success: (resp) => {
                    if (resp.error_message === "success") {
                        this.UpdateUser({
                            id: resp.id,
                            username: resp.username,
                            photo: resp.photo,
                            is_login: true
                        });
                        data.success()
                    } else {
                        data.error()
                    }
                }
            })
        },
        updatePhoto(url: string) {
            this.photo = url
        }
    }
})

interface Resp {
    success: () => void
    error: () => void | null
}

export interface User {
    id: string,
    username: string,
    photo: string,
    is_login: boolean
}

