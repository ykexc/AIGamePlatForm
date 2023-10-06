import {createRouter, createWebHistory} from 'vue-router'
import userUseStore from '@/stores/user'
import snakeUseStore from '@/stores/snake/record'
import gobangUseStore from '@/stores/gobang/record'
import {get} from "@/util/request";
import {ElMessage} from "element-plus";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'index',
            component: () => import('@/views/IndexView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/gamehall',
            name: 'gamehall',
            component: () => import('@/views/GameHallView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/user/account/login',
            name: 'login',
            component: () => import('@/views/user/account/UserAccountLoginView.vue'),
            meta: {
                requestAuth: false
            }
        },
        {
            path: '/404',
            name: "404",
            component: () => import('@/views/error/ErrorView.vue'),
            meta: {
                requestAuth: false
            }
        },
        {
            path: '/ranklist',
            name: 'ranklist',
            component: () => import('@/views/ranklist/RankListIndexView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/record',
            name: 'record',
            component: () => import('@/views/record/RecordIndexView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/snake/record/:recordId/',
            name: 'snake_record_content',
            component: () => import('@/views/record/snake/RecordContentView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/gobang/record/:recordId',
            name: 'gobang_record_content',
            component: () => import('@/views/record/gobang/RecordContentView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/snake/pk',
            name: 'snake_pk',
            component: () => import('@/views/pk/snake/PkIndexVIew.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/gobang/pk',
            name: 'gobang_pk',
            component: () => import('@/views/pk/gobang/PkIndexView.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: '/user/bot',
            name: 'user_bot_index',
            component: () => import('@/views/user/bot/UserBotIndex.vue'),
            meta: {
                requestAuth: true
            }
        },
        {
            path: "/:catchAll(.*)",
            redirect: "/404/"
        },
        {
            path: '/user/account/qq/receive_code',
            name: 'user_account_qq_receive_code',
            component: () => import('@/views/user/account/UserAccountQQReceiveCodeView.vue'),
            meta: {
                requestAuth: false
            }
        }
    ]
})


router.beforeEach((to, _from, next) => {
    const store = userUseStore()
    const snakeRecordStore = snakeUseStore()
    const gobangRecordStore = gobangUseStore()
    const token = localStorage.getItem("jwt_token");
    if (token) {
        store.UpdateToken(token)
        store.UpdateState(true)
        get({
            url: '/api/user/account/info/',
            isAuth: true,
            success: (resp) => {
                if (resp.error_message === "success") {
                    store.UpdateUser({
                        id: resp.id,
                        username: resp.username,
                        photo: resp.photo,
                        is_login: true
                    });
                } else {
                    ElMessage.error("系统异常")
                }
            }
        })
    }
    if (to.meta.requestAuth && !store.is_login) {
        next('/user/account/login')
    } else {
        if (to.path.startsWith('/snake/record/') && !snakeRecordStore.is_record)
            next('/record')
        else if (to.path.startsWith('/gobang/record/') && !gobangRecordStore.is_record)
            next('/record')
        else next()
    }
})

export default router
