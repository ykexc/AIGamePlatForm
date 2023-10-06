
<template>
    <div class="common-layout">
        <el-container>
            <el-main/>
            <el-aside width="230px" style="margin-top: 40px;margin-right: 40px">
                <div class="font-sty">欢迎注册King Of Bots平台</div>
                <div style="margin-top: 50px">
                    <el-input v-model="user.name" type="text" placeholder="请输入用户名">
                        <template #prefix>
                            <el-icon><User/></el-icon>
                        </template>
                    </el-input>
                </div>
                <div style="margin-top: 20px">
                    <el-input v-model="user.password" type="password" placeholder="请输入密码">
                        <template #prefix>
                            <el-icon><Lock/></el-icon>
                        </template>
                    </el-input>
                </div>
                <div style="margin-top: 20px">
                    <el-input v-model="user.repeatPassword" type="password" placeholder="确认密码">
                        <template #prefix>
                            <el-icon><Lock/></el-icon>
                        </template>
                    </el-input>
                </div>
                <div style="margin-top: 20px">
                    <el-button type="primary" @click="register" plain style="width: 100%">立即注册</el-button>
                </div>
            </el-aside>
        </el-container>
    </div>
</template>



<script setup lang="ts">

import {Lock, User} from "@element-plus/icons-vue";
import {reactive} from "vue";
import {post} from "@/util/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const user = reactive({
    name: '',
    password: '',
    repeatPassword: ''
})


const register = () => {
    post({url : '/api/user/account/register/', data : {
        username: user.name,
        password: user.password,
        confirmedPassword: user.repeatPassword
    }, isAuth : false, success : (data) => {
        if (data.error_message === "success") {
            ElMessage.success("注册成功")
            router.push('/user/account/login')
        } else {
            ElMessage.warning(data.error_message)
        }
    }})
}


</script>

<style scoped>

</style>