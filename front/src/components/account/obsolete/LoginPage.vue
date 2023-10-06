<template>

    <div class="common-layout">
        <el-container>
            <el-main/>
            <el-aside width="230px" style="margin-top: 40px;margin-right: 40px">
                <div class="font-sty">欢迎登录King Of Bots平台</div>
                <div style="margin-top: 50px">
                    <el-input v-model="user.username" type="text" placeholder="请输入用户名">
                        <template #prefix>
                            <el-icon>
                                <User/>
                            </el-icon>
                        </template>
                    </el-input>
                </div>
                <div style="margin-top: 20px">
                    <el-input v-model="user.password" type="password" placeholder="请输入密码">
                        <template #prefix>
                            <el-icon>
                                <Lock/>
                            </el-icon>
                        </template>
                    </el-input>
                </div>
                <div style="margin-top: 20px">
                    <el-button @click="login" type="success" plain style="width: 100%">登录</el-button>
                </div>
                <div class="other-login">第三方登录</div>
                <div style="text-align: center;margin-top: 3px">
                    <el-popover
                            placement="top-start"
                            :width="150"
                            trigger="hover"
                            content="qq一键登录"
                    >
                        <template #reference>
                            <el-image src="https://ykexc-1314584214.cos.ap-nanjing.myqcloud.com/202209081054778.png"
                                      style="cursor: pointer;height: 30px"/>
                        </template>
                    </el-popover>
                </div>

            </el-aside>
        </el-container>
    </div>


</template>

<script lang="ts" setup>
import {Lock} from "@element-plus/icons-vue";
import {reactive} from "vue";
import {ElMessage, ElNotification} from "element-plus";
import {post} from "@/util/request";
import router from "@/router";
import userUseStore from '@/stores/user'

const user = reactive({
    username: '',
    password: ''
})
const store = userUseStore()
const jwt_token = localStorage.getItem("jwt_token");
if (jwt_token) {
    store.UpdateToken(jwt_token)
    store.getInfo({
        success() {
            router.push('/snake/pk')
            store.UpdatePullingInfo(false)
        },
        error() {
            store.UpdatePullingInfo(false)
        }
    })
} else {
    store.UpdatePullingInfo(false)
}


const login = (): void => {
    if (!user.username || !user.password) {
        ElMessage.warning("请填写用户名或密码")
    } else {
        post({
            url: '/api/user/account/token/', data: {
                username: user.username,
                password: user.password
            }, isAuth: false, success: (data): void => {
                if (data.error_message === "success") {
                    localStorage.setItem("jwt_token", data.token)
                    store.UpdateToken(data.token)
                    store.getInfo({
                        success: () :void => {
                            router.push('/snake/pk')
                            ElNotification({
                                title: 'Success',
                                message: '欢迎回来',
                                type: 'success',
                                duration: 1000,
                                offset: 100
                            });
                        },
                        error: function (): void | null {
                            ElMessage.error("系统错误")
                        }
                    })
                } else {
                    ElMessage.error("用户名或密码错误")
                }
            }, error: (): void => {
                ElMessage.error("用户名或密码错误")
            }
        })
    }
}

</script>

<style>
.font-sty {
    text-align: center;
    font-weight: bold;
    color: grey;
    font-size: 16px;
}

.other-login {
    text-align: center;
    font-size: small;
    color: grey;
    margin-top: 5px;
}
</style>