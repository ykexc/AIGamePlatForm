import axios from 'axios'
import {AxiosError} from 'axios'
import {ElMessage} from 'element-plus'
import userUseStore from '@/stores/user'
import router from "@/router";

const defaultError = (err: Error | AxiosError): any => {
    if (err instanceof AxiosError) {
        if (err.config?.url === '/api/user/account/info/')
            router.push('/user/account/login')
        return
    }
    console.log(err)
    ElMessage.error('发生了一些错误，请联系管理员')
}

interface PostParams {
    url: string;
    data: object;
    isAuth: boolean;
    success: (data: any, status?: number) => void;
    error?: (err: Error | AxiosError) => void;
}

function post({url, data, isAuth, success, error = defaultError}: PostParams): void {
    const store = userUseStore()
    if (!isAuth) {
        axios.post(url, data, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
        }).then(({data}) => {
            success(data)
        }).catch(err => error(err))
    } else {
        axios.post(url, data, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: "Bearer " + store.token
            },
        }).then(({data}) => {
            success(data)
        }).catch(error)
    }
}


interface GetParams {
    url: string;
    isAuth: boolean;
    data?: any;
    success: (message: any, status?: number) => void;
    error?: (e: Error | AxiosError) => void;
}

function get({url, isAuth, data, success, error = defaultError}: GetParams): void {
    const store = userUseStore()
    if (!isAuth) {
        axios.get(url).then(({data}) => {
            success(data)
        }).catch(error)
    } else {
        if (data !== undefined) {
            axios.get(url, {
                headers: {
                    Authorization: "Bearer " + store.token
                },
                params: data
            }).then(({data}) => {
                success(data)
            }).catch(error)
        } else {
            axios.get(url, {
                headers: {
                    Authorization: "Bearer " + store.token
                },
            }).then(({data}) => {
                success(data)
            }).catch(error)
        }
    }
}

export {get, post}
